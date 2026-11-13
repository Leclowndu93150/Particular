package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SnowPuffParticle extends SingleQuadParticle
{
	protected final SpriteSet provider;

	protected SnowPuffParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.first());
		this.provider = provider;
		lifetime = 22 + random.nextInt(10);
		quadSize = 0.22f * (0.8f + random.nextFloat() * 0.5f);
		gravity = 0.4f;
		friction = 0.9f;
		setParticleSpeed(velX, velY, velZ);
		setSpriteFromAge(provider);
	}

	@Override
	public void tick()
	{
		super.tick();
		setSpriteFromAge(provider);
	}

	@Override
	protected SingleQuadParticle.Layer getLayer()
	{
		return SingleQuadParticle.Layer.OPAQUE;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet provider;

		public Factory(SpriteSet provider)
		{
			this.provider = provider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ, RandomSource random)
		{
			return new SnowPuffParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}
