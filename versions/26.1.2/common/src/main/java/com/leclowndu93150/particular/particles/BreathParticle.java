package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class BreathParticle extends SingleQuadParticle
{
	private static final float GROWTH = 2.5f;

	protected final SpriteSet provider;
	private final float startSize;
	private final float startAlpha;

	protected BreathParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.first());
		this.provider = provider;
		lifetime = 14 + random.nextInt(8);
		startSize = 0.04f + random.nextFloat() * 0.02f;
		startAlpha = 0.3f + random.nextFloat() * 0.15f;
		quadSize = startSize;
		alpha = startAlpha;
		gravity = 0f;
		friction = 0.9f;
		hasPhysics = false;
		setParticleSpeed(velX, velY, velZ);
		setSpriteFromAge(provider);
	}

	@Override
	public void tick()
	{
		super.tick();

		float progress = (float) age / lifetime;
		quadSize = startSize * (1f + progress * GROWTH);
		alpha = startAlpha * (1f - progress);
		setSpriteFromAge(provider);
	}

	@Override
	protected SingleQuadParticle.Layer getLayer()
	{
		return SingleQuadParticle.Layer.TRANSLUCENT;
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
			return new BreathParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}
