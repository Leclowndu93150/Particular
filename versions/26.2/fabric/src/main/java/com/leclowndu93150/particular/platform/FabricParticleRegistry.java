package com.leclowndu93150.particular.platform;

import com.leclowndu93150.particular.Constants;
import com.leclowndu93150.baguettelib.platform.services.IParticleRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FabricParticleRegistry implements IParticleRegistry {
    private static final Map<String, ParticleType<?>> PARTICLES = new HashMap<>();

    @Override
    public <T extends ParticleOptions> ParticleType<T> register(String name, Supplier<? extends ParticleType<T>> factory) {
        ParticleType<T> particle = Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, name),
                factory.get());
        PARTICLES.put(name, particle);
        return particle;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ParticleType<?>> T get(String name) {
        return (T) PARTICLES.get(name);
    }

    @Override
    public SimpleParticleType registerParticle(String name, boolean alwaysShow) {
        SimpleParticleType particle = Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, name),
                FabricParticleTypes.simple(alwaysShow));
        PARTICLES.put(name, particle);
        return particle;
    }
}
