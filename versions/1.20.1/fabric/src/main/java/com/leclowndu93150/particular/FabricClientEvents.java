package com.leclowndu93150.particular;

import com.leclowndu93150.particular.particles.*;
import com.leclowndu93150.particular.particles.leaves.BigLeafParticle;
import com.leclowndu93150.particular.particles.leaves.ConiferLeafParticle;
import com.leclowndu93150.particular.particles.leaves.LeafParticle;
import com.leclowndu93150.particular.particles.leaves.SpinningLeafParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashEmitterParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashFoamParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashRingParticle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class FabricClientEvents {
    private static int cascadeCleanupTicks = 0;

    public static void registerParticleFactories() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();

        registry.register(Particles.OAK_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.BIRCH_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.SPRUCE_LEAF(), ConiferLeafParticle.Factory::new);
        registry.register(Particles.JUNGLE_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.ACACIA_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.DARK_OAK_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.AZALEA_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.MANGROVE_LEAF(), LeafParticle.Factory::new);

        registry.register(Particles.WHITE_OAK_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.WHITE_SPRUCE_LEAF(), ConiferLeafParticle.Factory::new);

        registry.register(Particles.MAPLE_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.BRIMWOOD_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.RU_BAOBAB_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.KAPOK_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.EUCALYPTUS_LEAF(), BigLeafParticle.Factory::new);
        registry.register(Particles.REDWOOD_LEAF(), ConiferLeafParticle.Factory::new);
        registry.register(Particles.MAGNOLIA_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.RU_PALM_LEAF(), BigLeafParticle.Factory::new);
        registry.register(Particles.LARCH_LEAF(), ConiferLeafParticle.Factory::new);
        registry.register(Particles.GOLDEN_LARCH_LEAF(), ConiferLeafParticle.Factory::new);
        registry.register(Particles.SOCOTRA_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.BAMBOO_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.WILLOW_LEAF(), LeafParticle.Factory::new);
        registry.register(Particles.RU_CYPRESS_LEAF(), ConiferLeafParticle.Factory::new);

        registry.register(Particles.WW_BAOBAB_LEAF(), SpinningLeafParticle.Factory::new);
        registry.register(Particles.WW_CYPRESS_LEAF(), ConiferLeafParticle.Factory::new);
        registry.register(Particles.WW_PALM_LEAF(), BigLeafParticle.Factory::new);

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
        CommonClass.clientSetup();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            Level world = client.level;
            CommonClass.onClientTick(world);

            if (world != null && ++cascadeCleanupTicks >= 100) {
                cascadeCleanupTicks = 0;
                CommonClass.cleanupInvalidCascades(world);
            }
        });

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
