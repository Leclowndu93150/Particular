package com.leclowndu93150.particular.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import javax.annotation.Nullable;

public class FireballFlameParticleType extends ParticleType<WorldParticleOptions> {
    
    public FireballFlameParticleType() {
        super(true, new ParticleOptions.Deserializer<WorldParticleOptions>() {
            @Override
            public WorldParticleOptions fromCommand(ParticleType<WorldParticleOptions> type, StringReader reader) {
                return new WorldParticleOptions(type);
            }

            @Override
            public WorldParticleOptions fromNetwork(ParticleType<WorldParticleOptions> type, FriendlyByteBuf buf) {
                return new WorldParticleOptions(type);
            }
        });
    }

    @Override
    public Codec<WorldParticleOptions> codec() {
        return Codec.unit(() -> new WorldParticleOptions(this));
    }
    
    @Override
    public boolean getOverrideLimiter() {
        return true;
    }

    public static class Factory implements ParticleProvider<WorldParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(WorldParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneWorldParticle(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
        }
    }
}