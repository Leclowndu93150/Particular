package com.leclowndu93150.particular.particles.splashes;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;

public class WaterSplashRingParticle extends SingleQuadParticle
{
	protected final SpriteSet provider;
	private final float width;

	WaterSplashRingParticle(ClientLevel clientWorld, double x, double y, double z, float width, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.get(0, 18));
		gravity = 0;
		lifetime = 18;
		this.width = width;
		this.provider = provider;
		setSpriteFromAge(provider);
	}

	@Override
	protected SingleQuadParticle.Layer getLayer()
	{
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public void tick()
	{
		super.tick();

		setSpriteFromAge(provider);

		if (!level.getFluidState(BlockPos.containing(x, y, z)).is(FluidTags.WATER))
		{
			this.remove();
		}
	}

	@Override
	public float getQuadSize(float scaleFactor)
	{
		float ageDelta = age + scaleFactor;
		float progress = ageDelta / (float)lifetime;
		return width * (0.8f + 0.2f * progress);
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
		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double x, double y, double z, double g, double h, double i, RandomSource random)
		{
			return new WaterSplashRingParticle(clientWorld, x, y, z, (float) g, provider);
		}
	}
}