package com.leclowndu93150.particular.platform.services;

import net.minecraft.core.particles.SimpleParticleType;

public interface IParticleRegistry {
    SimpleParticleType registerParticle(String name, boolean alwaysShow);
    SimpleParticleType getParticle(String name);
}
