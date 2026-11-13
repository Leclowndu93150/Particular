package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class SnowFleckParticle extends SingleQuadParticle
{
	private static final float SHRINK_START = 0.75f;
	private static final float SWAY_SPEED = 0.3f;
	private static final double SWAY_STRENGTH = 0.0025;

	private final float swayPhase;

	protected SnowFleckParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, TextureAtlasSprite sprite)
	{
		super(clientWorld, x, y, z, sprite);
		lifetime = 30 + random.nextInt(25);
		quadSize = 0.05f + random.nextFloat() * 0.04f;
		gravity = 0.25f;
		friction = 0.94f;
		swayPhase = random.nextFloat() * Mth.TWO_PI;
		setParticleSpeed(velX, velY, velZ);
	}

	@Override
	public void tick()
	{
		xd += Mth.sin(age * SWAY_SPEED + swayPhase) * SWAY_STRENGTH;
		zd += Mth.cos(age * SWAY_SPEED + swayPhase) * SWAY_STRENGTH;
		super.tick();

		if (onGround || !level.getFluidState(BlockPos.containing(x, y, z)).isEmpty())
		{
			remove();
		}
	}

	@Override
	public float getQuadSize(float partialTicks)
	{
		float progress = Math.min(1f, (age + partialTicks) / lifetime);
		if (progress < SHRINK_START)
		{
			return quadSize;
		}

		float shrink = (progress - SHRINK_START) / (1f - SHRINK_START);
		return quadSize * (1f - shrink * shrink);
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
			return new SnowFleckParticle(world, x, y, z, velX, velY, velZ, provider.get(random));
		}
	}
}
