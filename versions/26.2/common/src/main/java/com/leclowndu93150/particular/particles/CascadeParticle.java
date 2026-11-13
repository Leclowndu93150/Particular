package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class CascadeParticle extends SingleQuadParticle
{
	private static final int LAST_GROWING_FRAME = 7;
	private static final int LAST_FRAME = 11;
	private static final float SHRINK_START = 0.65f;

	protected final SpriteSet provider;

	protected CascadeParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.get(0, 9));
		this.provider = provider;
		lifetime = 14 + random.nextInt(9);
		quadSize = 0.3f * (0.6f + random.nextFloat() * 0.8f);
		float brightness = 0.85f + random.nextFloat() * 0.15f;
		setColor(brightness, brightness, brightness);
		gravity = 0.12f;
		friction = 0.92f;
		setParticleSpeed(random.nextDouble() * 0.1f - 0.05f, 0, random.nextDouble() * 0.1f - 0.05f);
		setGrowingSprite();
		removeIfInsideSolidBlock();
	}

	@Override
	public void tick()
	{
		super.tick();

		removeIfInsideSolidBlock();

		setGrowingSprite();
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

	private void setGrowingSprite()
	{
		setSprite(provider.get(age * LAST_GROWING_FRAME, lifetime * LAST_FRAME));
	}

	private void removeIfInsideSolidBlock()
	{
		BlockPos pos = BlockPos.containing(new Vec3(x, y, z));
		if (level.getBlockState(pos).isRedstoneConductor(level, pos))
		{
			alpha = 0;
			remove();
		}
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
			return new CascadeParticle(world, x, y, z, provider);
		}
	}
}
