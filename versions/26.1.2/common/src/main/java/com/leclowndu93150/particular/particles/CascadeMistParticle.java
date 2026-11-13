package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class CascadeMistParticle extends SingleQuadParticle
{
	private final float startSize;
	private final float startAlpha;

	protected CascadeMistParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, TextureAtlasSprite sprite)
	{
		super(clientWorld, x, y, z, sprite);
		lifetime = 30 + random.nextInt(20);
		startSize = 0.3f + random.nextFloat() * 0.15f;
		startAlpha = 0.25f + random.nextFloat() * 0.15f;
		quadSize = startSize;
		alpha = startAlpha;
		gravity = 0f;
		friction = 0.96f;
		setParticleSpeed(velX, velY, velZ);
	}

	@Override
	public void tick()
	{
		super.tick();

		float progress = (float) age / lifetime;
		quadSize = startSize * (1f + progress * 1.5f);
		alpha = startAlpha * (1f - progress);
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
			return new CascadeMistParticle(world, x, y, z, velX, velY, velZ, provider.get(random));
		}
	}
}
