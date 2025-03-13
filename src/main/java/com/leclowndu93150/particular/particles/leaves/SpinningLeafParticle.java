package com.leclowndu93150.particular.particles.leaves;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SpinningLeafParticle extends LeafParticle
{
	protected float angleFactor;

	protected SpinningLeafParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet provider)
	{
		super(world, x, y, z, r, g, b, provider);

		angleFactor = (float) (Math.random() * Math.PI * 2.0);
		roll = getAngle();
	}


	@Override
	protected float getAngle()
	{
		int a = Math.max(0, age - fadeInDuration);
		return (angleFactor + a / (rotateFactor + (lifetime - a) / 100f) / 2f) * flippedDirection;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet provider;

		public Factory(SpriteSet provider)
		{
			this.provider = provider;
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ)
		{
			return new SpinningLeafParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}