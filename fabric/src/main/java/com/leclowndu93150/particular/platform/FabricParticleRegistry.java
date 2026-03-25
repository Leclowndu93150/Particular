package com.leclowndu93150.particular.platform;

import com.leclowndu93150.particular.Constants;
import com.leclowndu93150.baguettelib.platform.services.IParticleRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class FabricParticleRegistry implements IParticleRegistry {
    private static final Map<String, SimpleParticleType> PARTICLES = new HashMap<>();
    
    @Override
    public SimpleParticleType registerParticle(String name, boolean alwaysShow) {
        SimpleParticleType particle = Registry.register(BuiltInRegistries.PARTICLE_TYPE, 
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, name),
            FabricParticleTypes.simple(alwaysShow));
        PARTICLES.put(name, particle);
        return particle;
    }
    
    @Override
    public SimpleParticleType getParticle(String name) {
        return PARTICLES.get(name);
    }
}
