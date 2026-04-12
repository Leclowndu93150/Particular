package com.leclowndu93150.particular;

import com.leclowndu93150.particular.particles.splashes.WaterSplashParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterParticleGroupsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Constants.MOD_ID)
public class Main {

    public Main(IEventBus eventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ParticularConfig.COMMON_SPEC);
        
        NeoForgeParticles.getRegistry().register(eventBus);
        
        CommonClass.init();
        
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            ParticleEngine.RENDER_ORDER.add(WaterSplashParticle.WATER_SPLASH_TYPE);
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
            eventBus.addListener(NeoForgeClientEvents::registerParticleFactories);
            eventBus.addListener(NeoForgeClientEvents::clientSetup);
            eventBus.addListener(this::onRegisterParticuleGroup);
        }
    }

    public void onRegisterParticuleGroup(RegisterParticleGroupsEvent event){
        event.register(WaterSplashParticle.WATER_SPLASH_TYPE, WaterSplashParticle.WaterSplashParticleGroup::new);
    }

}
