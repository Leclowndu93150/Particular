package com.leclowndu93150.particular.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MoteParticle extends SingleQuadParticle
{
	private static final int FADE_IN_TICKS = 20;
	private static final int FADE_OUT_TICKS = 30;
	private static final float SWAY_SPEED = 0.06f;
	private static final double SWAY_STRENGTH = 0.0009;
	private static final float TWINKLE_SPEED = 0.25f;
	private static final float TWINKLE_DEPTH = 0.25f;
	private static final int GROUNDED_AGING = 4;

	private final float phase;
	private final float maxAlpha;
	private final double lift;

	protected MoteParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, TextureAtlasSprite sprite)
	{
		super(clientWorld, x, y, z, sprite);
		lifetime = 100 + random.nextInt(60);
		quadSize = 0.03f + random.nextFloat() * 0.025f;
		maxAlpha = 0.5f + random.nextFloat() * 0.3f;
		phase = random.nextFloat() * Mth.TWO_PI;
		alpha = 0f;
		gravity = 0f;
		friction = 0.98f;
		lift = velY;
		setParticleSpeed(velX, velY, velZ);
	}

	@Override
	public void tick()
	{
		float time = age * SWAY_SPEED + phase;
		xd += Mth.sin(time) * SWAY_STRENGTH;
		zd += Mth.cos(time * 0.8f) * SWAY_STRENGTH;
		yd += (lift - yd) * 0.05 + Mth.sin(time * 1.3f) * SWAY_STRENGTH * 0.5;
		super.tick();

		if (onGround)
		{
			age += GROUNDED_AGING;
		}
	}

	@Override
	public void extract(QuadParticleRenderState state, Camera camera, float partialTicks)
	{
		float time = age + partialTicks;
		float fade = Math.min(1f, Math.min(time / FADE_IN_TICKS, (lifetime - time) / FADE_OUT_TICKS));
		float twinkle = 1f - TWINKLE_DEPTH + TWINKLE_DEPTH * Mth.sin(time * TWINKLE_SPEED + phase);
		alpha = Math.max(0f, fade) * twinkle * maxAlpha;
		super.extract(state, camera, partialTicks);
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
			return new MoteParticle(world, x, y, z, velX, velY, velZ, provider.get(random));
		}
	}
}
