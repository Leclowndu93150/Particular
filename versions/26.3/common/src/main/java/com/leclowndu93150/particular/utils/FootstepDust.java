package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.baguettelib.util.BlockColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FootstepDust {
    private static final double RANGE_SQ = 32.0 * 32.0;
    private static final int RUN_INTERVAL = 4;
    private static final int SPRINT_DUST_INTERVAL = 2;
    private static final double RUN_SPEED = 0.3;
    private static final double LANDING_DROP = 0.35;
    private static final double SERVER_LANDING_DROP = 0.72;
    private static final float SERVER_LANDING_SPEED = 0.15f;
    private static final int SERVER_CHIPS_PER_PUFF = 12;
    private static final int MAX_SERVER_LANDING_PUFFS = 16;
    private static final double LANDING_RING_RADIUS = 0.3;
    private static final int MIN_LANDING_PUFFS = 4;
    private static final int MAX_LANDING_PUFFS = 12;
    private static final double TINT_LIFT = 0.12;

    private static List<? extends String> parsedFrom;
    private static Set<Block> dustyBlocks = Set.of();
    private static List<TagKey<Block>> dustyTags = List.of();

    public static void tick(ClientLevel level) {
        if (!ParticularConfig.footstepDust()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused()) return;

        ParticleStatus status = mc.options.particles().get();
        if (status == ParticleStatus.MINIMAL) return;

        boolean mobs = ParticularConfig.COMMON.footstepDustMobs.get();
        boolean sprintHook = ParticularConfig.COMMON.footstepDustReplaceSprint.get();
        boolean landingHook = ParticularConfig.COMMON.footstepDustReplaceLanding.get();
        RandomSource random = level.getRandom();

        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living) || !living.onGround()) continue;
            if (!mobs && !(living instanceof Player)) continue;

            double moveX = living.getX() - living.xo;
            double moveZ = living.getZ() - living.zo;
            double drop = living.yo - living.getY();
            double speed = Math.sqrt(moveX * moveX + moveZ * moveZ);

            boolean landed = drop > LANDING_DROP && !(landingHook && drop > SERVER_LANDING_DROP);
            boolean running = !(sprintHook && living.isSprinting())
                    && (living.isSprinting() || speed > RUN_SPEED)
                    && (living.tickCount + living.getId()) % RUN_INTERVAL == 0;
            if (!landed && !running) continue;
            if (status == ParticleStatus.DECREASED && !landed && random.nextBoolean()) continue;

            if (living.isSpectator() || living.isInvisible() || living.isInWater()) continue;
            if (living.distanceToSqr(mc.player) > RANGE_SQ) continue;

            BlockPos pos = dustyGround(level, living);
            if (pos == null) continue;

            double[] colour = dustColour(level, pos);
            if (landed) {
                int puffs = Mth.clamp((int) (MIN_LANDING_PUFFS + drop * 8), MIN_LANDING_PUFFS, MAX_LANDING_PUFFS);
                burst(mc, random, colour, puffs, living.getX(), living.getY() + 0.05, living.getZ(), living.getBbWidth() * 0.4);
            } else {
                spawn(mc, colour,
                        living.getX() + random.nextDouble() * 0.4 - 0.2,
                        living.getY() + 0.05,
                        living.getZ() + random.nextDouble() * 0.4 - 0.2,
                        -moveX * 0.15 + random.nextDouble() * 0.02 - 0.01,
                        0.02 + random.nextDouble() * 0.02,
                        -moveZ * 0.15 + random.nextDouble() * 0.02 - 0.01);
            }
        }
    }

    public static boolean replaceSprintParticle(Entity entity) {
        if (!ParticularConfig.footstepDust() || !ParticularConfig.COMMON.footstepDustReplaceSprint.get()) return false;
        if (!(entity.level() instanceof ClientLevel level)) return false;
        if (!(entity instanceof Player) && !ParticularConfig.COMMON.footstepDustMobs.get()) return false;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.particles().get() == ParticleStatus.MINIMAL) return false;
        if (entity.distanceToSqr(mc.player) > RANGE_SQ) return false;

        BlockPos pos = dustyGround(level, entity);
        if (pos == null) return false;

        if (entity.tickCount % SPRINT_DUST_INTERVAL == 0) {
            RandomSource random = level.getRandom();
            double moveX = entity.getX() - entity.xo;
            double moveZ = entity.getZ() - entity.zo;
            spawn(mc, dustColour(level, pos),
                    entity.getX() + (random.nextDouble() - 0.5) * entity.getBbWidth(),
                    entity.getY() + 0.05,
                    entity.getZ() + (random.nextDouble() - 0.5) * entity.getBbWidth(),
                    -moveX * 0.15 + random.nextDouble() * 0.02 - 0.01,
                    0.02 + random.nextDouble() * 0.03,
                    -moveZ * 0.15 + random.nextDouble() * 0.02 - 0.01);
        }
        return true;
    }

    public static boolean replaceLandingBurst(ClientboundLevelParticlesPacket packet) {
        if (!ParticularConfig.footstepDust() || !ParticularConfig.COMMON.footstepDustReplaceLanding.get()) return false;
        if (!(packet.particle() instanceof BlockParticleOption chips) || chips.getType() != ParticleTypes.BLOCK) return false;
        if (packet.count() <= 0 || packet.xMaxSpeed() != SERVER_LANDING_SPEED) return false;
        if (packet.xDist() != packet.yDist() || packet.xDist() != packet.zDist()) return false;
        if (!isDusty(chips.getState())) return false;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.options.particles().get() == ParticleStatus.MINIMAL) return false;
        if (mc.player.distanceToSqr(packet.x(), packet.y(), packet.z()) > RANGE_SQ) return false;

        BlockPos ground = BlockPos.containing(packet.x(), packet.y() - 0.2, packet.z());
        double[] colour = BlockColorUtil.getBlockTextureColor(chips.getState(), mc.level, ground);
        int puffs = Mth.clamp(packet.count() / SERVER_CHIPS_PER_PUFF, MIN_LANDING_PUFFS, MAX_SERVER_LANDING_PUFFS);
        burst(mc, mc.level.getRandom(), colour, puffs, packet.x(), packet.y() + 0.05, packet.z(), LANDING_RING_RADIUS);
        return true;
    }

    private static void burst(Minecraft mc, RandomSource random, double[] colour, int puffs, double x, double y, double z, double radius) {
        for (int i = 0; i < puffs; i++) {
            double angle = random.nextDouble() * Mth.TWO_PI;
            double speed = 0.04 + random.nextDouble() * 0.06;
            spawn(mc, colour,
                    x + Math.cos(angle) * radius,
                    y,
                    z + Math.sin(angle) * radius,
                    Math.cos(angle) * speed,
                    0.01 + random.nextDouble() * 0.03,
                    Math.sin(angle) * speed);
        }
    }

    private static BlockPos dustyGround(ClientLevel level, Entity entity) {
        BlockPos pos = entity.blockPosition();
        if (isDusty(level.getBlockState(pos))) return pos;

        pos = entity.getOnPos();
        return isDusty(level.getBlockState(pos)) ? pos : null;
    }

    private static void spawn(Minecraft mc, double[] colour, double x, double y, double z, double velX, double velY, double velZ) {
        SingleQuadParticle particle = (SingleQuadParticle) mc.particleEngine.createParticle(Particles.DUST_PUFF(), x, y, z, velX, velY, velZ);
        if (particle == null) return;

        particle.setColor(lift(colour[0]), lift(colour[1]), lift(colour[2]));
    }

    private static double[] dustColour(ClientLevel level, BlockPos pos) {
        return BlockColorUtil.getBlockTextureColor(level.getBlockState(pos), level, pos);
    }

    private static float lift(double channel) {
        return (float) (channel + (1.0 - channel) * TINT_LIFT);
    }

    private static boolean isDusty(BlockState state) {
        List<? extends String> entries = ParticularConfig.COMMON.footstepDustBlocks.get();
        if (entries != parsedFrom) {
            Set<Block> blocks = new HashSet<>();
            List<TagKey<Block>> tags = new ArrayList<>();
            for (String entry : entries) {
                String trimmed = entry.trim();
                boolean tag = trimmed.startsWith("#");
                Identifier id = Identifier.tryParse(tag ? trimmed.substring(1) : trimmed);
                if (id == null) continue;

                if (tag) {
                    tags.add(TagKey.create(Registries.BLOCK, id));
                } else {
                    BuiltInRegistries.BLOCK.getOptional(id).ifPresent(blocks::add);
                }
            }
            dustyBlocks = blocks;
            dustyTags = tags;
            parsedFrom = entries;
        }

        if (dustyBlocks.contains(state.getBlock())) return true;
        for (TagKey<Block> tag : dustyTags) {
            if (state.is(tag)) return true;
        }
        return false;
    }
}
