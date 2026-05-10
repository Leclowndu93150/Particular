package com.leclowndu93150.particular.platform.services;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public interface IParticleRegistry {
    <T extends ParticleOptions> ParticleType<T> register(String name, Supplier<? extends ParticleType<T>> factory);

    <T extends ParticleType<?>> T get(String name);

    SimpleParticleType registerParticle(String name, boolean alwaysShow);

    default SimpleParticleType getParticle(String name) {
        return get(name);
    }
}
