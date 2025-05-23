package com.leclowndu93150.particular.particles.leaves;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class ConiferLeafParticle extends LeafParticle
{
	protected ConiferLeafParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet provider)
	{
		super(world, x, y, z, r, g, b, provider);

		gravityFactor = 0.3f;
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
			return new ConiferLeafParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}