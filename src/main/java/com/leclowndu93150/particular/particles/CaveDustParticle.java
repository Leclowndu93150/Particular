package com.leclowndu93150.particular.particles;

import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.utils.Color;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CaveDustParticle extends BaseAshSmokeParticle
{
	protected CaveDustParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider)
	{
		super(world, x, y, z, 0, 0, 0, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0, ParticularConfig.COMMON.caveDustBaseMaxAge.get(), 0, true);

		Color color = Color.ofArgb(ParticularConfig.COMMON.caveDustColor.get());
		rCol = color.red();
		gCol = color.green();
		bCol = color.blue();

		gravity = (float) ((random.nextFloat() - 0.5f) * ParticularConfig.COMMON.caveDustMaxAcceleration.get());
	}

	@Override
	public void tick()
	{
		super.tick();

		if (random.nextInt(ParticularConfig.COMMON.caveDustAccelChangeChance.get()) == 0)
		{
			gravity = (random.nextFloat() - 0.5f) * ParticularConfig.COMMON.caveDustMaxAcceleration.get().floatValue();
		}

		int fadeDuration = ParticularConfig.COMMON.caveDustFadeDuration.get();

		if (age <= fadeDuration)
		{
			alpha = age / (float)fadeDuration;
		}
		else if (age > lifetime - fadeDuration)
		{
			alpha = (lifetime - age) / (float)fadeDuration;
		}
	}

	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet provider;

		public Factory(SpriteSet provider)
		{
			this.provider = provider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ)
		{
			return new CaveDustParticle(clientWorld, x, y, z, 0, 0, 0, 1.0F, provider);
		}
	}
}