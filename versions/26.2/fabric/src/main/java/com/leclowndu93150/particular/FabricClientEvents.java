package com.leclowndu93150.particular;

import com.leclowndu93150.particular.particles.*;
import com.leclowndu93150.particular.particles.leaves.ConiferLeafParticle;
import com.leclowndu93150.particular.particles.leaves.LeafParticle;
import com.leclowndu93150.particular.particles.leaves.SpinningLeafParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashEmitterParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashFoamParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashRingParticle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class FabricClientEvents {
    private static int cascadeCleanupTicks = 0;

    public static void registerParticleFactories() {
        ParticleProviderRegistry registry = ParticleProviderRegistry.getInstance();

        registry.register(Particles.OAK_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.BIRCH_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.SPRUCE_LEAF(), ConiferLeafParticle.Factory::new);
        registry.register(Particles.JUNGLE_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.ACACIA_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.DARK_OAK_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.AZALEA_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.MANGROVE_LEAF(), LeafParticle.Factory::new);

//        registry.register(Particles.WHITE_OAK_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.WHITE_SPRUCE_LEAF, ConiferLeafParticle.Factory::new);
//
//        registry.register(Particles.MAPLE_LEAF, SpinningLeafParticle.Factory::new);
//        registry.register(Particles.BRIMWOOD_LEAF, SpinningLeafParticle.Factory::new);
//        registry.register(Particles.RU_BAOBAB_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.KAPOK_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.EUCALYPTUS_LEAF, BigLeafParticle.Factory::new);
//        registry.register(Particles.REDWOOD_LEAF, ConiferLeafParticle.Factory::new);
//        registry.register(Particles.MAGNOLIA_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.RU_PALM_LEAF, BigLeafParticle.Factory::new);
//        registry.register(Particles.LARCH_LEAF, ConiferLeafParticle.Factory::new);
//        registry.register(Particles.GOLDEN_LARCH_LEAF, ConiferLeafParticle.Factory::new);
//        registry.register(Particles.SOCOTRA_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.BAMBOO_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.WILLOW_LEAF, LeafParticle.Factory::new);
//        registry.register(Particles.RU_CYPRESS_LEAF, ConiferLeafParticle.Factory::new);
//
//        registry.register(Particles.WW_BAOBAB_LEAF, SpinningLeafParticle.Factory::new);
//        registry.register(Particles.WW_CYPRESS_LEAF, ConiferLeafParticle.Factory::new);
//        registry.register(Particles.WW_PALM_LEAF, BigLeafParticle.Factory::new);

        registry.register(Particles.WATER_RIPPLE(), WaterRippleParticle.Factory::new);
        registry.register(Particles.ENDER_BUBBLE(), EnderBubbleParticle.Factory::new);
        registry.register(Particles.ENDER_BUBBLE_POP(), BubblePopParticle.Provider::new);
        registry.register(Particles.CAVE_DUST(), CaveDustParticle.Factory::new);
        registry.register(Particles.FIREFLY(), FireflyParticle.Factory::new);
        registry.register(Particles.WATERFALL_SPRAY(), WaterfallSprayParticle.Factory::new);
        registry.register(Particles.CASCADE(), CascadeParticle.Factory::new);

        registry.register(Particles.WATER_SPLASH_EMITTER(), WaterSplashEmitterParticle.Factory::new);
        registry.register(Particles.WATER_SPLASH(), WaterSplashParticle.Factory::new);
        registry.register(Particles.WATER_SPLASH_FOAM(), WaterSplashFoamParticle.Factory::new);
        registry.register(Particles.WATER_SPLASH_RING(), WaterSplashRingParticle.Factory::new);
        registry.register(Particles.CUBOID(), CuboidParticle.Factory::new);
    }

    public static void init() {
        ParticleEngine.RENDER_ORDER.add(WaterSplashParticle.WATER_SPLASH_TYPE);

        CommonClass.clientSetup();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            Level world = client.level;
            CommonClass.onClientTick(world);

            if (world != null && ++cascadeCleanupTicks >= 10) {
                cascadeCleanupTicks = 0;
                CommonClass.cleanupInvalidCascades(world);
            }
        });

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cascade_debug"), (GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player == null || mc.level == null) return;
                Font font = mc.font;
                String text = "Cascades: " + CommonClass.cascades.size();
                int textWidth = font.width(text);
                int x = 10;
                int y = 10;
                guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + font.lineHeight + 2, 0x80000000);
                guiGraphics.text(font, text, x, y, 0xFFFFFFFF);
            });
        }

        ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            CommonClass.onChunkLoad(world, chunk.getPos());
        });

        ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            if (!ParticularConfig.cascades() || !world.isClientSide()) return;

            LevelChunk levelChunk = (LevelChunk) chunk;
            var chunkPos = levelChunk.getPos();
            int minX = chunkPos.getMinBlockX();
            int maxX = chunkPos.getMaxBlockX();
            int minZ = chunkPos.getMinBlockZ();
            int maxZ = chunkPos.getMaxBlockZ();

            CommonClass.onChunkUnload(world, minX, maxX, minZ, maxZ);
        });
    }
}
