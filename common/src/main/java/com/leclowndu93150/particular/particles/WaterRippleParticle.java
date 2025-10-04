package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class WaterRippleParticle extends SingleQuadParticle
{
	protected final SpriteSet provider;

	protected WaterRippleParticle(ClientLevel world, double x, double y, double z, SpriteSet provider)
	{
		super(world, x, y, z, provider.get(0, 7));
		lifetime = 7;
		alpha = 0.2f;
		quadSize = 0.25f;
		this.provider = provider;
		setSpriteFromAge(provider);
	}

	@Override
	public void tick()
	{
		xo = x;
		yo = y;
		zo = z;
		if (age++ >= lifetime)
		{
			remove();
		}
		else
		{
			setSpriteFromAge(provider);
		}
	}

	@Override
	protected SingleQuadParticle.Layer getLayer()
	{
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public SingleQuadParticle.FacingCameraMode getFacingCameraMode()
	{
		return SingleQuadParticle.FacingCameraMode.LOOKAT_Y;
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
			return new WaterRippleParticle(world, x, y, z, provider);
		}
	}
}