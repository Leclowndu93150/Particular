package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class DustPuffParticle extends SingleQuadParticle
{
	private static final float GROWTH = 0.8f;
	private static final float SHRINK_START = 0.7f;

	protected final SpriteSet provider;

	protected DustPuffParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.first());
		this.provider = provider;
		lifetime = 16 + random.nextInt(10);
		quadSize = 0.1f + random.nextFloat() * 0.06f;
		gravity = 0.03f;
		friction = 0.88f;
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
	public float getQuadSize(float partialTicks)
	{
		float progress = Math.min(1f, (age + partialTicks) / lifetime);
		float size = quadSize * (1f + progress * GROWTH);
		if (progress < SHRINK_START)
		{
			return size;
		}

		float shrink = (progress - SHRINK_START) / (1f - SHRINK_START);
		return size * (1f - shrink * shrink);
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
			return new DustPuffParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}
