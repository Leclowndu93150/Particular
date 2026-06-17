package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class CascadeParticle extends SingleQuadParticle
{
	protected final SpriteSet provider;

	protected CascadeParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.get(0, 9));
		this.provider = provider;
		lifetime = 18;
		quadSize = 0.7f;
		gravity = 0.12f;
		friction = 0.92f;
		setParticleSpeed(random.nextDouble() * 0.1f - 0.05f, 0, random.nextDouble() * 0.1f - 0.05f);
		setSpriteFromAge(provider);
		removeIfInsideSolidBlock();
	}

	@Override
	public void tick()
	{
		super.tick();

		removeIfInsideSolidBlock();

		setSpriteFromAge(provider);
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
