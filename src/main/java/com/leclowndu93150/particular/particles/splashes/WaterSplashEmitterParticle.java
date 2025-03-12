package com.leclowndu93150.particular.particles.splashes;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.mixin.AccessorBillboardParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class WaterSplashEmitterParticle extends NoRenderParticle
{
	private final float speed;
	private final float width;
	private final float height;

	WaterSplashEmitterParticle(ClientLevel clientWorld, double x, double y, double z, float width, float speed)
	{
		super(clientWorld, x, y, z);
		speed = Math.min(2, speed);
		gravity = 0;
		lifetime = 24;
		this.speed = speed;
		this.width = width;
		this.height = (speed / 2f + width / 3f);

		clientWorld.addParticle(Particles.WATER_SPLASH.get(), x, y, z, width, this.height, 0);
		clientWorld.addParticle(Particles.WATER_SPLASH_FOAM.get(), x, y, z, width, this.height, 0);
		clientWorld.addParticle(Particles.WATER_SPLASH_RING.get(), x, y, z, width, 0, 0);

		if (speed > 0.5)
		{
			splash(width, (1.5f/8f + speed * 1/8f) + (width / 6f), 0.15f);
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
			level.addParticle(Particles.WATER_SPLASH.get(), x, y, z, width * 0.66f, height * 2f, 0);
			level.addParticle(Particles.WATER_SPLASH_FOAM.get(), x, y, z, width * 0.66f, height * 2f, 0);
			level.addParticle(Particles.WATER_SPLASH_RING.get(), x, y, z, width * 0.66f, 0, 0);
			splash(width * 0.66f, (3f/8f + speed * 1/8f) + (width / 6f), 0.05f);
		}

		if (!level.getFluidState(BlockPos.containing(x, y, z)).is(FluidTags.WATER))
		{
			this.remove();
		}
	}

	private void splash(float width, float speed, float spread)
	{
		for (int i = 0; i < width * 20f; ++i)
		{
			Particle droplet = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.FALLING_WATER,
				x, y + 1/16f, z, 0, 0, 0);
			if (droplet != null)
			{
				double xVel = random.triangle(0.0, spread);
				double yVel = speed * random.triangle(1.0, 0.25);
				double zVel = random.triangle(0.0, spread);
				droplet.setPos(x + xVel / spread * width, y + 1/16f, z + zVel / spread * width);
				droplet.setParticleSpeed(xVel, yVel, zVel);
				droplet.setColor(1f, 1f, 1f);
				((AccessorBillboardParticle) droplet).setQuadSize(1/8f);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		public Factory(SpriteSet provider) { }

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double x, double y, double z, double g, double h, double i)
		{
			return new WaterSplashEmitterParticle(clientWorld, x, y, z, (float) g, (float) h);
		}
	}
}