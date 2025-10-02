package com.leclowndu93150.particular;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Constants.MOD_ID)
public class Main {

    public Main(IEventBus eventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ParticularConfig.COMMON_SPEC);
        
        NeoForgeParticles.getRegistry().register(eventBus);
        
        CommonClass.init();
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
            eventBus.addListener(NeoForgeClientEvents::registerParticleFactories);
            eventBus.addListener(NeoForgeClientEvents::clientSetup);
        }
    }

}
