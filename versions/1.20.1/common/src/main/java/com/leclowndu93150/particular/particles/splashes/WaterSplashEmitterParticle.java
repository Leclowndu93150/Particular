package com.leclowndu93150.particular.particles.splashes;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.mixin.AccessorBillboardParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;

import javax.annotation.Nullable;

public class WaterSplashEmitterParticle extends NoRenderParticle
{
	private final float speed;
	private final float width;
	private final float height;
	private final boolean isLava;

	WaterSplashEmitterParticle(ClientLevel clientWorld, double x, double y, double z, float width, float speed, boolean lava)
	{
		super(clientWorld, x, y, z);
		speed = Math.min(2, speed);
		gravity = 0;
		lifetime = 24;
		this.speed = speed;
		this.width = width;
		this.height = (speed / 2f + width / 3f);
		this.isLava = lava;
		double flag = lava ? 1 : 0;

		clientWorld.addParticle(Particles.WATER_SPLASH(), x, y, z, width, this.height, flag);
		clientWorld.addParticle(Particles.WATER_SPLASH_FOAM(), x, y, z, width, this.height, flag);
		clientWorld.addParticle(Particles.WATER_SPLASH_RING(), x, y, z, width, 0, flag);

		if (speed > 0.5)
		{
			splash(width, (1.5f/8f + speed * 1/8f) + (width / 6f), 0.15f);
		}
		else if (ParticularConfig.COMMON.waterSplashSmallDroplets.get())
		{
			splash(width * 0.5f, (0.5f/8f + speed * 1/8f) + (width / 8f), 0.08f);
			remove();
		}
		else
		{
			remove();
		}
	}

	@Override
	public void tick()
	{
		super.tick();

		if (age == 8)
		{
			double flag = isLava ? 1 : 0;
			level.addParticle(Particles.WATER_SPLASH(), x, y, z, width * 0.66f, height * 2f, flag);
			level.addParticle(Particles.WATER_SPLASH_FOAM(), x, y, z, width * 0.66f, height * 2f, flag);
			level.addParticle(Particles.WATER_SPLASH_RING(), x, y, z, width * 0.66f, 0, flag);
			splash(width * 0.66f, (3f/8f + speed * 1/8f) + (width / 6f), 0.05f);
		}

		var fluid = level.getFluidState(BlockPos.containing(x, y, z));
		if (isLava ? !fluid.is(FluidTags.LAVA) : !fluid.is(FluidTags.WATER))
		{
			this.remove();
		}
	}

	private void splash(float width, float speed, float spread)
	{
		var dropletType = isLava ? ParticleTypes.LANDING_LAVA : ParticleTypes.FALLING_WATER;
		for (int i = 0; i < width * 20f; ++i)
		{
			Particle droplet = Minecraft.getInstance().particleEngine.createParticle(dropletType,
				x, y + 1/16f, z, 0, 0, 0);
			if (droplet != null)
			{
				double xVel = random.triangle(0.0, spread);
				double yVel = speed * random.triangle(1.0, 0.25);
				double zVel = random.triangle(0.0, spread);
				droplet.setPos(x + xVel / spread * width, y + 1/16f, z + zVel / spread * width);
				droplet.setParticleSpeed(xVel, yVel, zVel);
				if (!isLava) droplet.setColor(1f, 1f, 1f);
				((AccessorBillboardParticle) droplet).setQuadSize(1/8f);
			}
		}
	}

	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		public Factory(SpriteSet provider) { }

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double x, double y, double z, double g, double h, double i)
		{
			return new WaterSplashEmitterParticle(clientWorld, x, y, z, (float) g, (float) h, i > 0);
		}
	}
}
