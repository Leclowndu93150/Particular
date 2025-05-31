package com.leclowndu93150.particular;

import com.leclowndu93150.particular.compat.RegionsUnexplored;
import com.leclowndu93150.particular.compat.Traverse;
import com.leclowndu93150.particular.compat.WilderWild;
import com.leclowndu93150.particular.utils.CascadeData;
import com.leclowndu93150.particular.utils.LeafColorUtil;
import com.leclowndu93150.particular.utils.TextureCache;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static com.leclowndu93150.particular.Main.*;

public class ClientStuff {

    public static Map<Block, LeafData> leavesData = new HashMap<>();

    public static Color extractLeafColor(Level world, BlockPos pos, Block block) {
        BlockState state = block.defaultBlockState();
        try {
            if (world.getBlockState(pos).getBlock() == block) {
                state = world.getBlockState(pos);
            }
            double[] colorValues = LeafColorUtil.getBlockTextureColor(state, world, pos);
            return LeafColorUtil.getColorFromValues(colorValues);
        } catch (Exception e) {
            LOGGER.error("Failed to extract leaf color", e);
            return new Color(BiomeColors.getAverageFoliageColor(world, pos));
        }
    }

    public static void registerLeafData(Block block, LeafData leafData) {
        leavesData.put(block, leafData);
    }

    public static void registerLeafData(ResourceLocation id, LeafData leafData) {
        BuiltInRegistries.BLOCK.getOptional(id).ifPresent(block -> leavesData.put(block, leafData));
    }

    public static LeafData getLeafData(Block block) {
        return leavesData.getOrDefault(block, new LeafData(Particles.OAK_LEAF.get()));
    }

    public static class LeafData {
        private final ParticleOptions particle;
        private final BiFunction<Level, BlockPos, Color> colorBiFunc;

        public LeafData(ParticleOptions particle, BiFunction<Level, BlockPos, Color> colorBiFunc) {
            this.particle = particle;
            this.colorBiFunc = colorBiFunc;
        }

        public LeafData(ParticleOptions particle, Color color) {
            this(particle, (world, pos) -> color);
        }

        public LeafData(ParticleOptions particle) {
            this(particle, (world, pos) -> {
                Block block = world.getBlockState(pos).getBlock();
                return extractLeafColor(world, pos, block);
            });
        }


        public ParticleOptions getParticle() {
            return particle;
        }

        public Color getColor(Level world, BlockPos pos) {
            return colorBiFunc.apply(world, pos);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void clientSetup(final FMLClientSetupEvent event) {
            // Populate leaves data
            leavesData.put(Blocks.OAK_LEAVES, new LeafData(Particles.OAK_LEAF.get()));
            leavesData.put(Blocks.BIRCH_LEAVES, new LeafData(Particles.BIRCH_LEAF.get(), new Color(FoliageColor.getBirchColor())));
            leavesData.put(Blocks.SPRUCE_LEAVES, new LeafData(Particles.SPRUCE_LEAF.get(), new Color(FoliageColor.getEvergreenColor())));
            leavesData.put(Blocks.JUNGLE_LEAVES, new LeafData(Particles.JUNGLE_LEAF.get()));
            leavesData.put(Blocks.ACACIA_LEAVES, new LeafData(Particles.ACACIA_LEAF.get()));
            leavesData.put(Blocks.DARK_OAK_LEAVES, new LeafData(Particles.DARK_OAK_LEAF.get()));
            leavesData.put(Blocks.AZALEA_LEAVES, new LeafData(Particles.AZALEA_LEAF.get(), Color.white));
            leavesData.put(Blocks.FLOWERING_AZALEA_LEAVES, new LeafData(Particles.AZALEA_LEAF.get(), Color.white));
            leavesData.put(Blocks.MANGROVE_LEAVES, new LeafData(Particles.MANGROVE_LEAF.get()));
            leavesData.put(Blocks.CHERRY_LEAVES, new LeafData(null));

            // Mod compat
            if (ModList.get().isLoaded("traverse")) {
                Traverse.addLeaves();
            }

            if (ModList.get().isLoaded("regions_unexplored")) {
                RegionsUnexplored.addLeaves();
            }

            if (ModList.get().isLoaded("wilderwild")) {
                WilderWild.addLeaves();
            }

            event.enqueueWork(() -> {
                for (Block block : ForgeRegistries.BLOCKS) {
                    ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);

                    // Skip blocks we've already registered
                    if (leavesData.containsKey(block)) {
                        continue;
                    }

                    boolean isLeafBlock = block instanceof LeavesBlock ||
                            id.getPath().contains("leaves") ||
                            id.getPath().contains("leaf");

                    if (isLeafBlock) {

                        ParticleOptions particle = Particles.OAK_LEAF.get(); // Default fallback

                        if (id.getPath().contains("spruce") || id.getPath().contains("pine") ||
                                id.getPath().contains("fir") || id.getPath().contains("conifer")) {
                            particle = Particles.SPRUCE_LEAF.get();
                        } else if (id.getPath().contains("birch")) {
                            particle = Particles.BIRCH_LEAF.get();
                        } else if (id.getPath().contains("jungle")) {
                            particle = Particles.JUNGLE_LEAF.get();
                        } else if (id.getPath().contains("acacia")) {
                            particle = Particles.ACACIA_LEAF.get();
                        } else if (id.getPath().contains("dark_oak")) {
                            particle = Particles.DARK_OAK_LEAF.get();
                        } else if (id.getPath().contains("mangrove")) {
                            particle = Particles.MANGROVE_LEAF.get();
                        }

                        LeafData leafData = new LeafData(particle);
                        leavesData.put(block, leafData);
                    }
                }

            });
        }

        @SubscribeEvent
        public static void onResourcesReloaded(net.minecraftforge.client.event.TextureStitchEvent.Post event) {
            TextureCache.clear();
        }

        private static int cascadeCleanupTicks = 0;


        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Level world = Minecraft.getInstance().level;
            if (world == null || event.phase != TickEvent.Phase.START) return;

            RandomSource random = world.random;

            if (world.getDayTime() == ParticularConfig.COMMON.fireflyStartTime.get()) {
                var dailyRandomList = ParticularConfig.COMMON.fireflyDailyRandom.get();
                fireflyFrequency = dailyRandomList.get(random.nextInt(dailyRandomList.size())).floatValue();
            }

            if (!ParticularConfig.waterSplash()) return;

            Minecraft mc = Minecraft.getInstance();
            int renderDistance = mc.options.renderDistance().get();
            BlockPos playerPos = mc.player.blockPosition();
            long currentTime = world.getGameTime();

            cascades.entrySet().removeIf(entry -> {
                BlockPos pos = entry.getKey();
                CascadeData cascadeData = entry.getValue();

                int chunkDistance = Math.max(
                        Math.abs((pos.getX() >> 4) - (playerPos.getX() >> 4)),
                        Math.abs((pos.getZ() >> 4) - (playerPos.getZ() >> 4))
                );

                if (chunkDistance > renderDistance) {
                    return true;
                }

                if (currentTime - cascadeData.createdTime > 100) {
                    if (world.getFluidState(pos).is(Fluids.WATER) &&
                            world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) &&
                            world.getFluidState(pos.below()).is(Fluids.WATER)) {

                        int strength = 0;
                        if (world.getFluidState(pos.north()).is(Fluids.WATER)) { ++strength; }
                        if (world.getFluidState(pos.east()).is(Fluids.WATER)) { ++strength; }
                        if (world.getFluidState(pos.south()).is(Fluids.WATER)) { ++strength; }
                        if (world.getFluidState(pos.west()).is(Fluids.WATER)) { ++strength; }

                        if (strength > 0) {
                            boolean isEncased = !world.getBlockState(pos.above().north()).isAir() &&
                                    !world.getBlockState(pos.above().east()).isAir() &&
                                    !world.getBlockState(pos.above().south()).isAir() &&
                                    !world.getBlockState(pos.above().west()).isAir();

                            if (!isEncased) {
                                cascades.put(pos, new CascadeData(strength, currentTime));
                                return false;
                            }
                        }
                    }
                    return true;
                }

                if (!world.getFluidState(pos).is(Fluids.WATER) ||
                        !world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) ||
                        !world.getFluidState(pos.below()).is(Fluids.WATER)) {
                    return true;
                }

                float height = world.getFluidState(pos.above()).getOwnHeight();
                double x = pos.getX();
                double y = (double) pos.getY() + random.nextDouble() * height + 1;
                double z = pos.getZ();

                if (random.nextBoolean()) {
                    x += random.nextDouble();
                    z += random.nextIntBetweenInclusive(0, 1);
                } else {
                    x += random.nextIntBetweenInclusive(0, 1);
                    z += random.nextDouble();
                }

                Particle cascade = mc.particleEngine.createParticle(Particles.CASCADE.get(), x, y, z, 0, 0, 0);
                if (cascade != null) {
                    float size = cascadeData.strength / 4f * height;
                    cascade.scale(1f - (1f - size) / 2f);
                }

                return false;
            });

            if (++cascadeCleanupTicks >= 100) {
                cascadeCleanupTicks = 0;
                cleanupInvalidCascades(world);
            }
        }

        @SubscribeEvent
        public static void onRenderHUD(RenderGuiOverlayEvent.Post event) {
            if(FMLLoader.isProduction()) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Font font = mc.font;

            String cascadeText = "Cascades: " + cascades.size();
            int textWidth = font.width(cascadeText);
            int x = 10;
            int y = 10;

            guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + font.lineHeight + 2, 0x80000000);

            guiGraphics.drawString(font, cascadeText, x, y, 0xFFFFFF);
        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            Level world = (Level) event.getLevel();
            if (!ParticularConfig.cascades() || !world.isClientSide()) return;

            ResourceLocation newDimension = world.dimensionType().effectsLocation();
            if (currentDimension != null && !newDimension.equals(currentDimension)) {
                Main.LOGGER.debug("Dimension changed from {} to {}, clearing cascades", currentDimension, newDimension);
                cascades.clear();
            }
            currentDimension = newDimension;

            CompletableFuture.runAsync(() -> {
                ChunkAccess chunk = event.getChunk();
                final int minX = chunk.getPos().getMinBlockX();
                final int minZ = chunk.getPos().getMinBlockZ();
                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

                List<Pair<BlockPos, FluidState>> waterBlocks = new ArrayList<>();

                for (int sectionIndex = 0; sectionIndex < chunk.getSectionsCount(); sectionIndex++) {
                    LevelChunkSection section = chunk.getSection(sectionIndex);

                    if (section == null || section.hasOnlyAir()) continue;
                    if (!section.maybeHas(state -> state.getFluidState().is(Fluids.WATER))) continue;

                    int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
                    int baseY = sectionY << 4;

                    PalettedContainer<BlockState> states = section.getStates();

                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            for (int x = 0; x < 16; x++) {
                                BlockState state = states.get(x, y, z);
                                FluidState fluidState = state.getFluidState();

                                if (fluidState.is(Fluids.WATER)) {
                                    int worldX = minX + x;
                                    int worldY = baseY + y;
                                    int worldZ = minZ + z;

                                    waterBlocks.add(Pair.of(
                                            new BlockPos(worldX, worldY, worldZ),
                                            fluidState
                                    ));
                                }
                            }
                        }
                    }
                }

                if (!waterBlocks.isEmpty()) {
                    Minecraft.getInstance().execute(() -> {
                        for (Pair<BlockPos, FluidState> pair : waterBlocks) {
                            Main.updateCascade(world, pair.getLeft(), pair.getRight());
                        }
                    });
                }
            }, Util.backgroundExecutor());
        }

        @SubscribeEvent
        public static void onChunkUnload(ChunkEvent.Unload event) {
            if (!ParticularConfig.cascades() || !event.getLevel().isClientSide()) return;

            var chunkPos = event.getChunk().getPos();
            int minX = chunkPos.getMinBlockX();
            int maxX = chunkPos.getMaxBlockX();
            int minZ = chunkPos.getMinBlockZ();
            int maxZ = chunkPos.getMaxBlockZ();

            cascades.entrySet().removeIf(entry -> {
                BlockPos pos = entry.getKey();
                return pos.getX() >= minX && pos.getX() <= maxX &&
                        pos.getZ() >= minZ && pos.getZ() <= maxZ;
            });
        }

        @SubscribeEvent
        public static void onLevelUnload(LevelEvent.Unload event) {
            if (event.getLevel().isClientSide()) {
                cascades.clear();
            }
        }
    }

    private static void cleanupInvalidCascades(Level world) {
        if (!ParticularConfig.cascades()) {
            cascades.clear();
            return;
        }

        long currentTime = world.getGameTime();

        cascades.entrySet().removeIf(entry -> {
            BlockPos pos = entry.getKey();
            CascadeData cascadeData = entry.getValue();

            if (!world.hasChunkAt(pos)) {
                return true;
            }

            if (currentTime - cascadeData.createdTime > 100) {
                return true;
            }

            FluidState currentState = world.getFluidState(pos);
            FluidState aboveState = world.getFluidState(pos.above());
            FluidState belowState = world.getFluidState(pos.below());

            boolean isValid = currentState.is(Fluids.WATER) &&
                    aboveState.is(Fluids.FLOWING_WATER) &&
                    belowState.is(Fluids.WATER);

            return !isValid;
        });
    }
}