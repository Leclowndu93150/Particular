package com.leclowndu93150.particular;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.fml.config.ModConfig;

public class Main implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, ParticularConfig.COMMON_SPEC);
        Particles.init();
        CommonClass.init();
    }

    @Override
    public void onInitializeClient() {
        FabricClientEvents.registerParticleFactories();
        FabricClientEvents.init();
    }
}
