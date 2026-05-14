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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ForgeParticles.OAK_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.BIRCH_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.SPRUCE_LEAF.get(), ConiferLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.JUNGLE_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.ACACIA_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.DARK_OAK_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.AZALEA_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.MANGROVE_LEAF.get(), LeafParticle.Factory::new);

        event.registerSpriteSet(ForgeParticles.WHITE_OAK_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WHITE_SPRUCE_LEAF.get(), ConiferLeafParticle.Factory::new);

        event.registerSpriteSet(ForgeParticles.MAPLE_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.BRIMWOOD_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.RU_BAOBAB_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.KAPOK_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.EUCALYPTUS_LEAF.get(), BigLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.REDWOOD_LEAF.get(), ConiferLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.MAGNOLIA_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.RU_PALM_LEAF.get(), BigLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.LARCH_LEAF.get(), ConiferLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.GOLDEN_LARCH_LEAF.get(), ConiferLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.SOCOTRA_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.BAMBOO_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WILLOW_LEAF.get(), LeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.RU_CYPRESS_LEAF.get(), ConiferLeafParticle.Factory::new);

        event.registerSpriteSet(ForgeParticles.WW_BAOBAB_LEAF.get(), SpinningLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WW_CYPRESS_LEAF.get(), ConiferLeafParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WW_PALM_LEAF.get(), BigLeafParticle.Factory::new);

        event.registerSpriteSet(ForgeParticles.WATER_RIPPLE.get(), WaterRippleParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.ENDER_BUBBLE.get(), EnderBubbleParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.ENDER_BUBBLE_POP.get(), BubblePopParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.CAVE_DUST.get(), CaveDustParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.FIREFLY.get(), FireflyParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WATERFALL_SPRAY.get(), WaterfallSprayParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.CASCADE.get(), CascadeParticle.Factory::new);

        event.registerSpriteSet(ForgeParticles.WATER_SPLASH_EMITTER.get(), WaterSplashEmitterParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WATER_SPLASH.get(), WaterSplashParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WATER_SPLASH_FOAM.get(), WaterSplashFoamParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.WATER_SPLASH_RING.get(), WaterSplashRingParticle.Factory::new);
        event.registerSpriteSet(ForgeParticles.CUBOID.get(), CuboidParticle.Factory::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CommonClass::clientSetup);
    }

    @SubscribeEvent
    public static void onResourcesReloaded(TextureStitchEvent.Post event) {
        CommonClass.onResourcesReloaded();
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {
        private static int cascadeCleanupTicks = 0;

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.START) return;
            Level world = Minecraft.getInstance().level;
            CommonClass.onClientTick(world);

            if (world != null && ++cascadeCleanupTicks >= 100) {
                cascadeCleanupTicks = 0;
                CommonClass.cleanupInvalidCascades(world);
            }
        }

        @SubscribeEvent
        public static void onRenderHUD(RenderGuiOverlayEvent.Post event) {
            if (FMLLoader.isProduction()) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Font font = mc.font;

            String cascadeText = "Cascades: " + CommonClass.cascades.size();
            int textWidth = font.width(cascadeText);
            int x = 10;
            int y = 10;

            guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + font.lineHeight + 2, 0x80000000);
            guiGraphics.drawString(font, cascadeText, x, y, 0xFFFFFF);
        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            Level world = (Level) event.getLevel();
            CommonClass.onChunkLoad(world, event.getChunk().getPos());
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
