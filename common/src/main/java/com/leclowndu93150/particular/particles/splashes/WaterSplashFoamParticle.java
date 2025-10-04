package com.leclowndu93150.particular.particles.splashes;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class WaterSplashFoamParticle extends WaterSplashParticle
{
	WaterSplashFoamParticle(ClientLevel clientWorld, double x, double y, double z, float width, float height, SpriteSet provider)
	{
		super(clientWorld, x, y, z, width, height, provider);
		colored = false;
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
			return new WaterSplashFoamParticle(clientWorld, x, y, z, (float) g, (float) h, provider);
		}
	}
}