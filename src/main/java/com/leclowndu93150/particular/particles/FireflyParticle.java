package com.leclowndu93150.particular.particles;

import com.leclowndu93150.particular.mixin.AccessorParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FireflyParticle extends TextureSheetParticle
{
	private static final int minOffTime = 20 * 2;
	private static final int maxOffTime = 20 * 4;
	private static final int minOnTime = 10;
	private static final int maxOnTime = 20;

	private final SimplexNoise noise;
	private int ageOffset = 0;
	private int ticksUntilNextSwitch = 40;
	private boolean isOn = false;

	protected FireflyParticle(ClientLevel world, double x, double y, double z, SpriteSet provider)
	{
		super(world, x, y, z);
		pickSprite(provider);

		gravity = 0;
		xd = 0;
		yd = 0;
		zd = 0;

		alpha = 0;
		rCol = 187f/255f;
		gCol = 1f;
		bCol = 107f/255f;

		if (world.getRandom().nextInt(10) == 1)
		{
			rCol = 107f/255f;
			gCol = 250/255f;
			bCol = 1f;

			if (world.getRandom().nextInt(10) == 1)
			{
				rCol = 1f;
				gCol = 124/255f;
				bCol = 107/255f;
			}
		}

		lifetime = 200;
		quadSize = 1/4f;

		noise = new SimplexNoise(random);
	}

	@Override
	public void tick()
	{
		super.tick();

		if (onGround)
		{
			((AccessorParticle) this).setStopped(false);
			ageOffset += 5;
		}

		if (--ticksUntilNextSwitch <= 0)
		{
			if (isOn)
			{
				isOn = false;
				ticksUntilNextSwitch = random.nextIntBetweenInclusive(minOffTime, maxOffTime);
			}
			else
			{
				isOn = true;
				ticksUntilNextSwitch = random.nextIntBetweenInclusive(minOnTime, maxOnTime);
			}
		}

		alpha = isOn && (lifetime - age) > 3  ? Math.min(1, alpha + 0.33f) : Math.max(0, alpha - 0.33f);

		// Broad movement
		float speedFactor = 1 / 10f;
		float noiseFactor = 1 / 300f;
		xd = noise.getValue(age * noiseFactor, age * noiseFactor) * speedFactor;
		yd = noise.getValue((age + ageOffset) * noiseFactor - 50f, (age + ageOffset) * noiseFactor + 100f) * speedFactor * 0.5f;
		zd = noise.getValue(age * noiseFactor + 100f, age * noiseFactor - 50f) * speedFactor;

		// Granular movement
		speedFactor = 1 / (10f + (float) Math.sin(Math.PI + age / 30f) * 2f);
		noiseFactor = 1 / 30f;
		xd += noise.getValue(age * noiseFactor, age * noiseFactor) * speedFactor * 0.5f;
		yd += noise.getValue((age + ageOffset) * noiseFactor - 50f, (age + ageOffset) * noiseFactor + 100f) * speedFactor;
		zd += noise.getValue(age * noiseFactor + 100f, age * noiseFactor - 50f) * speedFactor * 0.5f;
	}

	@Override
	protected int getLightColor(float tint)
	{
		return 15728880;
	}

	@Override
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

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ)
		{
			return new FireflyParticle(world, x, y, z, provider);
		}
	}
}