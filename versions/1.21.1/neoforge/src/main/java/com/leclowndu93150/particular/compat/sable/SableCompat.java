package com.leclowndu93150.particular.compat.sable;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.particles.CuboidParticle;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.math.BoundingBox3dc;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import dev.ryanhcode.sable.sublevel.plot.PlotChunkHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SableCompat {
    private static final boolean LOADED = ModList.get().isLoaded("sable");

    private static final int HULL_SCAN_INTERVAL = 20;
    private static final int SHAPE_SIGNATURE_INTERVAL = 40;
    private static final int CENTER_FALLBACK_MAX_FOOTPRINT = 4;
    private static final int MAX_VELOCITY_SAMPLES = 24;

    private static final double LOCAL_SCAN_INFLATE = 0.35;
    private static final double SURFACE_SCAN_MARGIN = 0.65;
    // A full block's center is half a block above the water when its bottom face first touches the surface.
    private static final double CONTACT_ABOVE_WATERLINE = 0.55;
    private static final double CONTACT_BELOW_WATERLINE = 1.05;
    private static final double CENTER_FALLBACK_TOLERANCE = 0.6;
    private static final double FOOTPRINT_SWEEP_MARGIN = 0.75;
    private static final double MIN_SPLASH_SPEED = 0.04;
    private static final double MIN_TRAIL_SPEED = 0.06;

    private static final int ENTRY_SPLASH_COOLDOWN = 15;
    private static final int TRAIL_SPLASH_COOLDOWN = 20;

    private static final int NO_FLUID_RESCAN_INTERVAL = 10;
    private static final double NO_FLUID_RESCAN_MOVE_SQ = 1.0;
    private static final int STATE_PRUNE_AGE = 200;

    private static final Map<UUID, SubLevelSplashState> STATES = new HashMap<>();

    public static boolean isLoaded() {
        return LOADED;
    }

    public static void tickSubLevelWake(SubLevel subLevel) {
        if (!LOADED || !ParticularConfig.waterSplash() || subLevel.isRemoved()) return;

        Level level = subLevel.getLevel();
        if (!level.isClientSide) return;

        UUID id = subLevel.getUniqueId();
        if (id == null) return;

        long gameTime = level.getGameTime();
        pruneStaleStates(gameTime);

        {
            BoundingBox3dc bounds = subLevel.boundingBox();
            SubLevelSplashState state = STATES.computeIfAbsent(id, k -> new SubLevelSplashState());
            state.lastSeenTime = gameTime;
            double centerY = centerY(bounds);
            double centerX = (bounds.minX() + bounds.maxX()) * 0.5;
            double centerZ = (bounds.minZ() + bounds.maxZ()) * 0.5;
            if (!state.touchingWater && gameTime < state.noFluidRescanTime
                    && sqDist(centerX, centerZ, state.noFluidCenterX, state.noFluidCenterZ) < NO_FLUID_RESCAN_MOVE_SQ) {
                rememberBounds(state, bounds, centerY);
                return;
            }

            double centerYDelta = Double.isNaN(state.lastCenterY) ? 0.0 : Math.abs(centerY - state.lastCenterY);
            boolean shapeChanged = checkShapeChanged(subLevel, state, gameTime);
            Motion centerMotion = sampleCenterMotion(level, subLevel).withVerticalAtLeast(centerYDelta);

            FluidSurface surface = findFluidSurfaceNearBounds(level, bounds, state);
            if (surface == null) {
                state.noFluidRescanTime = gameTime + NO_FLUID_RESCAN_INTERVAL;
                state.noFluidCenterX = centerX;
                state.noFluidCenterZ = centerZ;
                handleNoWaterContact(level, subLevel, bounds, state, gameTime, Double.NaN);
                rememberBounds(state, bounds, centerY);
                return;
            }

            double footprintTolerance = footprintTolerance(centerMotion.verticalSpeed);
            if (!sweptBoundsTouchesSurface(bounds, state, surface.height, footprintTolerance)) {
                handleNoWaterContact(level, subLevel, bounds, state, gameTime, surface.height);
                rememberBounds(state, bounds, centerY);
                return;
            }

            boolean waterLevelChanged = Double.isNaN(state.lastWaterY) || Math.abs(state.lastWaterY - surface.height) > 0.25;
            boolean movingVertically = centerMotion.verticalSpeed >= MIN_SPLASH_SPEED;
            if (shapeChanged
                    || state.contactBlocks.isEmpty()
                    || state.footprintBlocks.isEmpty()
                    || gameTime >= state.nextHullScanTime
                    || waterLevelChanged
                    || movingVertically) {
                HullScan scan = scanHullAtWaterline(level, subLevel, surface.height);
                state.contactBlocks = scan.contactBlocks;
                state.footprintBlocks = scanHullFootprintBlocks(level, subLevel, surface.height, footprintTolerance);
                state.nextHullScanTime = gameTime + HULL_SCAN_INTERVAL;
            }

            List<Vec3> positions = projectBlocks(level, state.footprintBlocks, surface.height, footprintTolerance);
            if (positions.isEmpty()) {
                positions = projectBlocks(level, state.contactBlocks, surface.height, CONTACT_BELOW_WATERLINE);
            }

            int footprintSize = positions.size();
            if (positions.isEmpty()) {
                int estimatedFootprint = estimateFootprintFromBounds(bounds);
                Vec3 fallback = centerFallbackPosition(bounds, surface.height, estimatedFootprint);
                if (fallback != null) {
                    positions = List.of(fallback);
                    footprintSize = estimatedFootprint;
                }
            }

            if (positions.isEmpty()) {
                handleNoWaterContact(level, subLevel, bounds, state, gameTime, surface.height);
                rememberBounds(state, bounds, centerY);
                return;
            }

            Motion motion = sampleMotion(level, subLevel, sampleBlocks(state), centerMotion);
            double impactSpeed = Math.max(motion.verticalSpeed, motion.horizontalSpeed);
            boolean justEnteredWater = !state.touchingWater;

            if (justEnteredWater && impactSpeed >= MIN_SPLASH_SPEED && gameTime >= state.nextEntrySplashTime) {
                spawnSplashBurst(level, positions, surface.height, impactSpeed, footprintSize);
                state.nextEntrySplashTime = gameTime + ENTRY_SPLASH_COOLDOWN;
            } else if (motion.verticalSpeed >= MIN_SPLASH_SPEED && gameTime >= state.nextEntrySplashTime) {
                spawnSplashBurst(level, positions, surface.height, motion.verticalSpeed, footprintSize);
                state.nextEntrySplashTime = gameTime + ENTRY_SPLASH_COOLDOWN;
            }

            if (motion.horizontalSpeed >= MIN_TRAIL_SPEED && gameTime >= state.nextTrailSplashTime) {
                spawnTrailSplash(level, positions, surface.height, motion.horizontalSpeed, footprintSize);
                state.nextTrailSplashTime = gameTime + TRAIL_SPLASH_COOLDOWN;
            }

            state.touchingWater = true;
            state.lastWaterY = surface.height;
            state.lastPositions = positions;
            state.footprintSize = footprintSize;
            rememberBounds(state, bounds, centerY);
        }
    }

    private static void pruneStaleStates(long gameTime) {
        STATES.values().removeIf(state -> gameTime - state.lastSeenTime > STATE_PRUNE_AGE);
    }

    private static double sqDist(double x1, double z1, double x2, double z2) {
        if (Double.isNaN(x2) || Double.isNaN(z2)) return Double.POSITIVE_INFINITY;
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz;
    }

    private static void handleNoWaterContact(Level level, SubLevel subLevel, BoundingBox3dc bounds, SubLevelSplashState state, long gameTime, double fallbackWaterY) {
        double waterY = Double.isNaN(state.lastWaterY) ? fallbackWaterY : state.lastWaterY;
        if (state.touchingWater && !Double.isNaN(waterY) && gameTime >= state.nextExitSplashTime) {
            Motion motion = sampleMotion(level, subLevel, sampleBlocks(state), sampleCenterMotion(level, subLevel));
            double exitSpeed = Math.max(motion.verticalSpeed, motion.horizontalSpeed);
            if (exitSpeed >= MIN_SPLASH_SPEED) {
                List<Vec3> positions = state.lastPositions.isEmpty()
                        ? List.of(boundsCenter(bounds, waterY))
                        : state.lastPositions;
                int footprintSize = state.footprintSize > 0 ? state.footprintSize : estimateFootprintFromBounds(bounds);
                spawnSplashBurst(level, positions, waterY, exitSpeed, footprintSize);
                state.nextExitSplashTime = gameTime + ENTRY_SPLASH_COOLDOWN;
            }
        }

        state.touchingWater = false;
        state.contactBlocks = List.of();
        state.footprintBlocks = List.of();
        state.lastPositions = List.of();
        state.footprintSize = 0;
        state.nextHullScanTime = 0L;
    }

    private static Motion sampleCenterMotion(Level level, SubLevel subLevel) {
        Vec3 localCenter = localBoundsCenter(subLevel);
        Vec3 velocityPerTick;
        if (localCenter != null) {
            velocityPerTick = Sable.HELPER.getVelocity(level, subLevel, localCenter).scale(1.0 / 20.0);
        } else {
            velocityPerTick = posePosition(subLevel.logicalPose()).subtract(posePosition(subLevel.lastPose()));
        }
        return Motion.fromVelocity(velocityPerTick);
    }

    private static Motion sampleMotion(Level level, SubLevel subLevel, List<BlockPos> blocks, Motion fallback) {
        if (blocks.isEmpty()) return fallback;

        double horizontalSpeed = fallback.horizontalSpeed;
        double verticalSpeed = fallback.verticalSpeed;
        double totalSpeed = fallback.totalSpeed;
        int step = Math.max(1, blocks.size() / MAX_VELOCITY_SAMPLES);
        int samples = 0;

        for (int i = 0; i < blocks.size() && samples < MAX_VELOCITY_SAMPLES; i += step) {
            Vec3 velocityPerTick = Sable.HELPER.getVelocity(level, subLevel, Vec3.atCenterOf(blocks.get(i))).scale(1.0 / 20.0);
            horizontalSpeed = Math.max(horizontalSpeed, velocityPerTick.horizontalDistance());
            verticalSpeed = Math.max(verticalSpeed, Math.abs(velocityPerTick.y));
            totalSpeed = Math.max(totalSpeed, velocityPerTick.length());
            samples++;
        }

        return new Motion(horizontalSpeed, verticalSpeed, totalSpeed);
    }

    private static List<BlockPos> sampleBlocks(SubLevelSplashState state) {
        return state.footprintBlocks.isEmpty() ? state.contactBlocks : state.footprintBlocks;
    }

    private static Vec3 localBoundsCenter(SubLevel subLevel) {
        BoundingBox3i bounds = computeLoadedBounds(subLevel);
        if (bounds == null) return null;

        return new Vec3(
                (bounds.minX() + bounds.maxX() + 1.0) * 0.5,
                (bounds.minY() + bounds.maxY() + 1.0) * 0.5,
                (bounds.minZ() + bounds.maxZ() + 1.0) * 0.5
        );
    }

    private static Vec3 posePosition(Pose3dc pose) {
        var pos = pose.position();
        return new Vec3(pos.x(), pos.y(), pos.z());
    }

    private static double centerY(BoundingBox3dc bounds) {
        return (bounds.minY() + bounds.maxY()) * 0.5;
    }

    private static Vec3 boundsCenter(BoundingBox3dc bounds, double waterY) {
        return new Vec3(
                (bounds.minX() + bounds.maxX()) * 0.5,
                waterY,
                (bounds.minZ() + bounds.maxZ()) * 0.5
        );
    }

    private static Vec3 centerFallbackPosition(BoundingBox3dc bounds, float waterY, int estimatedFootprint) {
        if (estimatedFootprint > CENTER_FALLBACK_MAX_FOOTPRINT) return null;

        double centerY = centerY(bounds);
        double distanceFromSurface = centerY - waterY;
        if (distanceFromSurface > CONTACT_ABOVE_WATERLINE || Math.abs(distanceFromSurface) > CENTER_FALLBACK_TOLERANCE) {
            return null;
        }

        return boundsCenter(bounds, waterY);
    }

    private static double footprintTolerance(double verticalSpeed) {
        return Math.max(CONTACT_BELOW_WATERLINE, verticalSpeed + FOOTPRINT_SWEEP_MARGIN);
    }

    private static boolean sweptBoundsTouchesSurface(BoundingBox3dc bounds, SubLevelSplashState state, float waterY, double belowTolerance) {
        double minY = bounds.minY();
        double maxY = bounds.maxY();
        if (!Double.isNaN(state.lastMinY)) {
            minY = Math.min(minY, state.lastMinY);
            maxY = Math.max(maxY, state.lastMaxY);
        }
        return minY <= waterY + CONTACT_ABOVE_WATERLINE && maxY >= waterY - belowTolerance;
    }

    private static void rememberBounds(SubLevelSplashState state, BoundingBox3dc bounds, double centerY) {
        state.lastCenterY = centerY;
        state.lastMinY = bounds.minY();
        state.lastMaxY = bounds.maxY();
    }

    private static void spawnSplashBurst(Level level, List<Vec3> positions, double waterY, double speed, int footprintSize) {
        if (positions.isEmpty()) return;

        float syntheticWidth = footprintToWidth(footprintSize);
        float velocity = (float) Math.min(2.0, speed * 5.0);

        Vec3 center = footprintCenter(positions, waterY);
        level.addParticle(Particles.WATER_SPLASH_EMITTER(), center.x, waterY, center.z, syntheticWidth, velocity, 0.0);

        if (footprintSize > 2) {
            int dropletCount = (int) Math.min(40, footprintSize * speed * 4.0);
            spawnDroplets(level, positions, (float) waterY, velocity, dropletCount);
        }
    }

    private static void spawnTrailSplash(Level level, List<Vec3> positions, double waterY, double speed, int footprintSize) {
        if (positions.isEmpty()) return;

        float syntheticWidth = footprintToWidth(footprintSize) * 0.5f;
        float velocity = (float) Math.min(1.5, speed * 3.0);

        Vec3 center = footprintCenter(positions, waterY);
        level.addParticle(Particles.WATER_SPLASH_EMITTER(), center.x, waterY, center.z, syntheticWidth, velocity, 0.0);
    }

    private static Vec3 footprintCenter(List<Vec3> positions, double waterY) {
        if (positions.size() == 1) return positions.get(0);

        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Vec3 pos : positions) {
            minX = Math.min(minX, pos.x);
            maxX = Math.max(maxX, pos.x);
            minZ = Math.min(minZ, pos.z);
            maxZ = Math.max(maxZ, pos.z);
        }

        return new Vec3((minX + maxX) * 0.5, waterY, (minZ + maxZ) * 0.5);
    }

    private static float footprintToWidth(int footprintSize) {
        return (float) Math.max(0.6, Math.min(4.0, Math.sqrt(Math.max(1, footprintSize)) * 0.5));
    }

    private static void spawnDroplets(Level level, List<Vec3> positions, float waterY, float velocity, int count) {
        if (count <= 0) return;

        boolean useCuboid = ParticularConfig.COMMON.cuboidSplashDroplets.get();
        var random = level.random;
        int spawned = 0;
        for (Vec3 pos : positions) {
            if (spawned >= count) break;

            double spread = 0.15;
            double xVel = random.triangle(0.0, spread);
            double yVel = (velocity * 0.3f) * random.triangle(1.0, 0.25);
            double zVel = random.triangle(0.0, spread);
            if (useCuboid) {
                level.addParticle(CuboidParticle.whiteSplash(), pos.x, waterY + 0.0625, pos.z, xVel, yVel, zVel);
            } else {
                level.addParticle(ParticleTypes.FALLING_WATER, pos.x, waterY + 0.0625, pos.z, xVel, yVel, zVel);
            }
            spawned++;
        }
    }

    private static int estimateFootprintFromBounds(BoundingBox3dc bounds) {
        double width = Math.max(1.0, bounds.maxX() - bounds.minX());
        double depth = Math.max(1.0, bounds.maxZ() - bounds.minZ());
        return Math.max(1, (int) (width * depth));
    }

    public static void invalidateShape(LevelPlot plot) {
        if (!LOADED) return;
        invalidateShape(plot.getSubLevel());
    }

    public static void invalidateShapeFromPlot(Object plotObj) {
        if (!LOADED || !(plotObj instanceof LevelPlot plot)) return;
        invalidateShape(plot);
    }

    public static void invalidateShape(SubLevel subLevel) {
        UUID id = subLevel.getUniqueId();
        if (id == null) return;

        SubLevelSplashState state = STATES.get(id);
        if (state == null) return;

        clearShapeCache(state);
    }

    private static boolean checkShapeChanged(SubLevel subLevel, SubLevelSplashState state, long gameTime) {
        if (gameTime < state.nextShapeCheckTime) return false;

        state.nextShapeCheckTime = gameTime + SHAPE_SIGNATURE_INTERVAL;
        long sig = computeSignature(subLevel);
        if (state.shapeSignature == sig) return false;

        clearShapeCache(state);
        state.shapeSignature = sig;
        return true;
    }

    private static void clearShapeCache(SubLevelSplashState state) {
        state.contactBlocks = List.of();
        state.footprintBlocks = List.of();
        state.lastPositions = List.of();
        state.nextHullScanTime = 0L;
        state.shapeSignature = Long.MIN_VALUE;
    }

    private static HullScan scanHullAtWaterline(Level level, SubLevel subLevel, float worldWaterY) {
        BoundingBox3i localBounds = computeLoadedBounds(subLevel);
        if (localBounds == null) return HullScan.EMPTY;

        Pose3dc pose = getScanPose(subLevel);
        AABB worldSlice = subLevel.boundingBox().toMojang().setMinY(worldWaterY).setMaxY(worldWaterY);
        AABB localSlice = worldToLocal(worldSlice, pose).inflate(LOCAL_SCAN_INFLATE);

        ScanBounds scanBounds = clampScanBounds(localSlice, localBounds);
        if (scanBounds.isEmpty()) return HullScan.EMPTY;

        List<BlockPos> contacts = new ArrayList<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int y = scanBounds.minY; y <= scanBounds.maxY; y++) {
            for (int x = scanBounds.minX; x <= scanBounds.maxX; x++) {
                for (int z = scanBounds.minZ; z <= scanBounds.maxZ; z++) {
                    mutable.set(x, y, z);
                    if (!isSolidHullBlock(level, mutable)) continue;

                    Vec3 worldPos = Sable.HELPER.projectOutOfSubLevel(level, Vec3.atCenterOf(mutable));
                    if (!isInWaterlineContactBand(worldPos.y, worldWaterY, CONTACT_BELOW_WATERLINE)) continue;

                    contacts.add(new BlockPos(x, y, z));
                }
            }
        }

        return new HullScan(contacts);
    }

    private static List<BlockPos> scanHullFootprintBlocks(Level level, SubLevel subLevel, float worldWaterY, double maxDistanceBelowWater) {
        BoundingBox3i localBounds = computeLoadedBounds(subLevel);
        if (localBounds == null) return List.of();

        Pose3dc pose = getScanPose(subLevel);
        AABB worldSlice = subLevel.boundingBox().toMojang().setMinY(worldWaterY).setMaxY(worldWaterY);
        AABB localSlice = worldToLocal(worldSlice, pose).inflate(LOCAL_SCAN_INFLATE);

        ScanBounds sliceBounds = clampScanBounds(localSlice, localBounds);
        if (sliceBounds.minX > sliceBounds.maxX || sliceBounds.minZ > sliceBounds.maxZ) return List.of();

        ScanBounds scanBounds = new ScanBounds(
                sliceBounds.minX,
                localBounds.minY(),
                sliceBounds.minZ,
                sliceBounds.maxX,
                localBounds.maxY(),
                sliceBounds.maxZ
        );

        Map<Long, FootprintCandidate> candidates = new HashMap<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int y = scanBounds.minY; y <= scanBounds.maxY; y++) {
            for (int x = scanBounds.minX; x <= scanBounds.maxX; x++) {
                for (int z = scanBounds.minZ; z <= scanBounds.maxZ; z++) {
                    mutable.set(x, y, z);
                    if (!isSolidHullBlock(level, mutable)) continue;

                    Vec3 worldPos = Sable.HELPER.projectOutOfSubLevel(level, Vec3.atCenterOf(mutable));
                    if (!isInWaterlineContactBand(worldPos.y, worldWaterY, maxDistanceBelowWater)) continue;

                    long key = columnKey(x, z);
                    double distance = Math.abs(worldPos.y - worldWaterY);
                    FootprintCandidate candidate = candidates.get(key);
                    if (candidate == null || distance < candidate.distance) {
                        candidates.put(key, new FootprintCandidate(new BlockPos(x, y, z), distance));
                    }
                }
            }
        }

        List<BlockPos> footprint = new ArrayList<>(candidates.size());
        for (FootprintCandidate candidate : candidates.values()) {
            footprint.add(candidate.pos);
        }
        return footprint;
    }

    private static List<Vec3> projectBlocks(Level level, List<BlockPos> blocks, float worldWaterY, double maxDistanceBelowWater) {
        List<Vec3> positions = new ArrayList<>(blocks.size());
        for (BlockPos block : blocks) {
            Vec3 worldPos = Sable.HELPER.projectOutOfSubLevel(level, Vec3.atCenterOf(block));
            if (isInWaterlineContactBand(worldPos.y, worldWaterY, maxDistanceBelowWater)) {
                positions.add(new Vec3(worldPos.x, worldWaterY, worldPos.z));
            }
        }
        return positions;
    }

    private static boolean isInWaterlineContactBand(double worldY, float worldWaterY, double maxDistanceBelowWater) {
        double distance = worldY - worldWaterY;
        return distance <= CONTACT_ABOVE_WATERLINE && distance >= -maxDistanceBelowWater;
    }

    private static long columnKey(int x, int z) {
        return ((long) x << 32) ^ (z & 0xffffffffL);
    }

    private static FluidSurface findFluidSurfaceNearBounds(Level level, BoundingBox3dc bounds, SubLevelSplashState state) {
        double minY = bounds.minY() - SURFACE_SCAN_MARGIN;
        double maxY = bounds.maxY() + SURFACE_SCAN_MARGIN;
        if (!Double.isNaN(state.lastMinY)) {
            minY = Math.min(minY, state.lastMinY - SURFACE_SCAN_MARGIN);
            maxY = Math.max(maxY, state.lastMaxY + SURFACE_SCAN_MARGIN);
        }

        double centerX = (bounds.minX() + bounds.maxX()) * 0.5;
        double centerZ = (bounds.minZ() + bounds.maxZ()) * 0.5;
        double[] xs = new double[]{centerX, bounds.minX(), bounds.maxX()};
        double[] zs = new double[]{centerZ, bounds.minZ(), bounds.maxZ()};

        for (double x : xs) {
            for (double z : zs) {
                FluidSurface surface = probeFluidAt(level, x, z, minY, maxY);
                if (surface != null) return surface;
            }
        }

        int minX = (int) Math.floor(bounds.minX());
        int maxX = (int) Math.ceil(bounds.maxX());
        int minZ = (int) Math.floor(bounds.minZ());
        int maxZ = (int) Math.ceil(bounds.maxZ());

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                FluidSurface surface = probeFluidAt(level, x + 0.5, z + 0.5, minY, maxY);
                if (surface != null) return surface;
            }
        }

        return null;
    }

    private static FluidSurface probeFluidAt(Level level, double x, double z, double minY, double maxY) {
        if (Sable.HELPER.getContaining(level, x, z) != null) return null;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = (int) Math.ceil(maxY); y >= (int) Math.floor(minY); y--) {
            pos.set((int) Math.floor(x), y, (int) Math.floor(z));
            FluidSurface surface = getFluidSurface(level, pos);
            if (surface != null) return surface;
        }
        return null;
    }

    private static FluidSurface getFluidSurface(Level level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        if (fluid.isEmpty() || !fluid.isSource() || !fluid.is(FluidTags.WATER)) return null;
        if (!level.getFluidState(pos.above()).isEmpty()) return null;

        return new FluidSurface((float) (pos.getY() + fluid.getHeight(level, pos)));
    }

    private static boolean isSolidHullBlock(Level level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        if (!fluid.isEmpty()) return false;

        BlockState block = level.getBlockState(pos);
        return !block.isAir() && !block.getCollisionShape(level, pos).isEmpty();
    }

    private static BoundingBox3i computeLoadedBounds(SubLevel subLevel) {
        BoundingBox3ic plotBounds = subLevel.getPlot().getBoundingBox();
        if (!isEmpty(plotBounds)) {
            return new BoundingBox3i(plotBounds);
        }

        BoundingBox3i bounds = null;
        BoundingBox3i temp = new BoundingBox3i();
        for (PlotChunkHolder chunk : subLevel.getPlot().getLoadedChunks()) {
            BoundingBox3ic chunkBounds = chunk.getBoundingBox();
            if (chunkBounds == null) continue;

            ChunkPos chunkPos = chunk.getPos();
            chunkBounds.move(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ(), temp);
            if (bounds == null) {
                bounds = new BoundingBox3i(temp);
            } else {
                bounds.expandTo(temp, bounds);
            }
        }
        return bounds;
    }

    private static long computeSignature(SubLevel subLevel) {
        long sig = 1125899906842597L;
        BoundingBox3ic plotBounds = subLevel.getPlot().getBoundingBox();
        if (!isEmpty(plotBounds)) {
            sig = sig * 31L + plotBounds.minX();
            sig = sig * 31L + plotBounds.minY();
            sig = sig * 31L + plotBounds.minZ();
            sig = sig * 31L + plotBounds.maxX();
            sig = sig * 31L + plotBounds.maxY();
            sig = sig * 31L + plotBounds.maxZ();
        }

        for (PlotChunkHolder chunk : subLevel.getPlot().getLoadedChunks()) {
            ChunkPos cp = chunk.getPos();
            sig = sig * 31L + cp.x;
            sig = sig * 31L + cp.z;

            BoundingBox3ic bounds = chunk.getBoundingBox();
            if (bounds == null) {
                sig = sig * 31L - 1L;
                continue;
            }

            sig = sig * 31L + bounds.minX();
            sig = sig * 31L + bounds.minY();
            sig = sig * 31L + bounds.minZ();
            sig = sig * 31L + bounds.maxX();
            sig = sig * 31L + bounds.maxY();
            sig = sig * 31L + bounds.maxZ();
        }
        return sig;
    }

    private static boolean isEmpty(BoundingBox3ic bounds) {
        return bounds == null || bounds.minX() > bounds.maxX() || bounds.minY() > bounds.maxY() || bounds.minZ() > bounds.maxZ();
    }

    private static Pose3dc getScanPose(SubLevel subLevel) {
        if (subLevel instanceof ClientSubLevelAccess client) {
            return client.renderPose();
        }
        return subLevel.logicalPose();
    }

    private static AABB worldToLocal(AABB world, Pose3dc pose) {
        Vec3[] corners = {
                new Vec3(world.minX, world.minY, world.minZ),
                new Vec3(world.minX, world.minY, world.maxZ),
                new Vec3(world.minX, world.maxY, world.minZ),
                new Vec3(world.minX, world.maxY, world.maxZ),
                new Vec3(world.maxX, world.minY, world.minZ),
                new Vec3(world.maxX, world.minY, world.maxZ),
                new Vec3(world.maxX, world.maxY, world.minZ),
                new Vec3(world.maxX, world.maxY, world.maxZ)
        };

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Vec3 corner : corners) {
            Vec3 local = pose.transformPositionInverse(corner);
            minX = Math.min(minX, local.x);
            minY = Math.min(minY, local.y);
            minZ = Math.min(minZ, local.z);
            maxX = Math.max(maxX, local.x);
            maxY = Math.max(maxY, local.y);
            maxZ = Math.max(maxZ, local.z);
        }

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static ScanBounds clampScanBounds(AABB localSlice, BoundingBox3i localBounds) {
        int minX = Math.max((int) Math.floor(localSlice.minX), localBounds.minX());
        int maxX = Math.min((int) Math.ceil(localSlice.maxX), localBounds.maxX());
        int minY = Math.max((int) Math.floor(localSlice.minY), localBounds.minY());
        int maxY = Math.min((int) Math.ceil(localSlice.maxY), localBounds.maxY());
        int minZ = Math.max((int) Math.floor(localSlice.minZ), localBounds.minZ());
        int maxZ = Math.min((int) Math.ceil(localSlice.maxZ), localBounds.maxZ());

        if (minY > maxY) {
            minY = localBounds.minY();
            maxY = localBounds.maxY();
        }

        return new ScanBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static class SubLevelSplashState {
        List<BlockPos> contactBlocks = List.of();
        List<BlockPos> footprintBlocks = List.of();
        List<Vec3> lastPositions = List.of();
        long nextHullScanTime = 0L;
        long nextShapeCheckTime = 0L;
        long nextEntrySplashTime = 0L;
        long nextExitSplashTime = 0L;
        long nextTrailSplashTime = 0L;
        long shapeSignature = Long.MIN_VALUE;
        double lastWaterY = Double.NaN;
        double lastCenterY = Double.NaN;
        double lastMinY = Double.NaN;
        double lastMaxY = Double.NaN;
        boolean touchingWater = false;
        int footprintSize = 0;
        long noFluidRescanTime = Long.MIN_VALUE;
        double noFluidCenterX = Double.NaN;
        double noFluidCenterZ = Double.NaN;
        long lastSeenTime = Long.MIN_VALUE;
    }

    private static class Motion {
        final double horizontalSpeed;
        final double verticalSpeed;
        final double totalSpeed;

        Motion(double horizontalSpeed, double verticalSpeed, double totalSpeed) {
            this.horizontalSpeed = horizontalSpeed;
            this.verticalSpeed = verticalSpeed;
            this.totalSpeed = totalSpeed;
        }

        static Motion fromVelocity(Vec3 velocity) {
            return new Motion(velocity.horizontalDistance(), Math.abs(velocity.y), velocity.length());
        }

        Motion withVerticalAtLeast(double verticalSpeed) {
            if (this.verticalSpeed >= verticalSpeed) return this;
            return new Motion(this.horizontalSpeed, verticalSpeed, Math.max(this.totalSpeed, verticalSpeed));
        }
    }

    private static class HullScan {
        static final HullScan EMPTY = new HullScan(List.of());

        final List<BlockPos> contactBlocks;

        HullScan(List<BlockPos> contactBlocks) {
            this.contactBlocks = contactBlocks;
        }
    }

    private static class FootprintCandidate {
        final BlockPos pos;
        final double distance;

        FootprintCandidate(BlockPos pos, double distance) {
            this.pos = pos;
            this.distance = distance;
        }
    }

    private static class ScanBounds {
        final int minX;
        final int minY;
        final int minZ;
        final int maxX;
        final int maxY;
        final int maxZ;

        ScanBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

        boolean isEmpty() {
            return minX > maxX || minY > maxY || minZ > maxZ;
        }
    }

    private static class FluidSurface {
        final float height;

        FluidSurface(float height) {
            this.height = height;
        }
    }
}
