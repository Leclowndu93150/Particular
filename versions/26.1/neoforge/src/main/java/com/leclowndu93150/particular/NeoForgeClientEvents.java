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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(NeoForgeParticles.OAK_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.BIRCH_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.SPRUCE_LEAF.get(), ConiferLeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.JUNGLE_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.ACACIA_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.DARK_OAK_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.AZALEA_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.MANGROVE_LEAF.get(), LeafParticle.Factory::new);

//        event.registerSpriteSet(NeoForgeParticles.WHITE_OAK_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.WHITE_SPRUCE_LEAF.get(), ConiferLeafParticle.Factory::new);
//
//        event.registerSpriteSet(NeoForgeParticles.MAPLE_LEAF.get(), SpinningLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.BRIMWOOD_LEAF.get(), SpinningLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.RU_BAOBAB_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.KAPOK_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.EUCALYPTUS_LEAF.get(), BigLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.REDWOOD_LEAF.get(), ConiferLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.MAGNOLIA_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.RU_PALM_LEAF.get(), BigLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.LARCH_LEAF.get(), ConiferLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.GOLDEN_LARCH_LEAF.get(), ConiferLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.SOCOTRA_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.BAMBOO_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.WILLOW_LEAF.get(), LeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.RU_CYPRESS_LEAF.get(), ConiferLeafParticle.Factory::new);
//
//        event.registerSpriteSet(NeoForgeParticles.WW_BAOBAB_LEAF.get(), SpinningLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.WW_CYPRESS_LEAF.get(), ConiferLeafParticle.Factory::new);
//        event.registerSpriteSet(NeoForgeParticles.WW_PALM_LEAF.get(), BigLeafParticle.Factory::new);

        event.registerSpriteSet(NeoForgeParticles.WATER_RIPPLE.get(), WaterRippleParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.ENDER_BUBBLE.get(), EnderBubbleParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.ENDER_BUBBLE_POP.get(), BubblePopParticle.Provider::new);
        event.registerSpriteSet(NeoForgeParticles.CAVE_DUST.get(), CaveDustParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.FIREFLY.get(), FireflyParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.WATERFALL_SPRAY.get(), WaterfallSprayParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.CASCADE.get(), CascadeParticle.Factory::new);

        event.registerSpriteSet(NeoForgeParticles.WATER_SPLASH_EMITTER.get(), WaterSplashEmitterParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.WATER_SPLASH.get(), WaterSplashParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.WATER_SPLASH_FOAM.get(), WaterSplashFoamParticle.Factory::new);
        event.registerSpriteSet(NeoForgeParticles.WATER_SPLASH_RING.get(), WaterSplashRingParticle.Factory::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CommonClass::clientSetup);
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {
        private static int cascadeCleanupTicks = 0;

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Pre event) {
            Level world = Minecraft.getInstance().level;
            CommonClass.onClientTick(world);

            if (world != null && ++cascadeCleanupTicks >= 100) {
                cascadeCleanupTicks = 0;
                CommonClass.cleanupInvalidCascades(world);
            }
        }

        @SubscribeEvent
        public static void onRenderHUD(RenderGuiEvent.Post event) {
            if (FMLLoader.getCurrent().isProduction()) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;

            GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
            Font font = mc.font;

            String cascadeText = "Cascades: " + CommonClass.cascades.size();
            int textWidth = font.width(cascadeText);
            int x = 10;
            int y = 10;

            guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + font.lineHeight + 2, 0x80000000);
            guiGraphics.text(font, cascadeText, x, y, 0xFFFFFF);
        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            Level world = (Level) event.getLevel();
            CommonClass.onChunkLoad(world);
        }

        @SubscribeEvent
        public static void onChunkUnload(ChunkEvent.Unload event) {
            Level world = (Level) event.getLevel();
            if (!ParticularConfig.cascades() || !world.isClientSide()) return;

            var chunkPos = event.getChunk().getPos();
            int minX = chunkPos.getMinBlockX();
            int maxX = chunkPos.getMaxBlockX();
            int minZ = chunkPos.getMinBlockZ();
            int maxZ = chunkPos.getMaxBlockZ();

            CommonClass.onChunkUnload(world, minX, maxX, minZ, maxZ);
        }

        @SubscribeEvent
        public static void onLevelUnload(LevelEvent.Unload event) {
            CommonClass.onLevelUnload((Level) event.getLevel());
        }
    }
}
