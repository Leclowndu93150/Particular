package com.leclowndu93150.particular.particles.leaves;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LeafParticle extends SingleQuadParticle
{
	protected static final int fadeInDuration = 10;
	protected static final int rampUpDuration = 20;
	protected static final int fadeOutDuration = 100;

	protected float rotateFactor;
	protected float gravityFactor = 0.075f;
	protected final boolean flippedSprite;
	protected final int flippedDirection;
	protected boolean expiring = false;

	protected LeafParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet provider)
	{
		super(world, x, y, z, provider.get(0, 1));

		hasPhysics = true;
		gravity = 0;
		lifetime = 200;

		xd = 0;
		yd = 0;
		zd = 0;

		alpha = 0;
		rCol = (float) r;
		gCol = (float) g;
		bCol = (float) b;
		rotateFactor = 6f + ((float) Math.random() * 3f);
		flippedSprite = random.nextBoolean();
		flippedDirection = random.nextBoolean() ? 1 : -1;

		quadSize = 7f / 32f;
	}

	protected float clamp(float value, float min, float max)
	{
		return Math.max(min, Math.min(max, value));
	}

	protected float getAngle()
	{
		int time = age - fadeInDuration;
		float speed = rotateFactor;
		float amplitude = clamp(time, 0, 30) / 30f;
		return (float) Math.sin(time / speed) * amplitude * 0.5f * flippedDirection;
	}

	@Override
	public void tick()
	{
		super.tick();

		if (age <= fadeInDuration)
		{
			yd = 0;
		}
		else if (!expiring && (onGround || yd == 0))
		{
			expiring = true;
			age = lifetime - fadeOutDuration;
			y += 0.01d;
			if (ParticularConfig.COMMON.fallingLeavesLayFlatOnGround.get())
			{
				if (ParticularConfig.COMMON.fallingLeavesLayFlatRightAngles.get())
				{
					roll = (float)(random.nextInt(4) / 2.0 * Math.PI);
				}
				else
				{
					roll = (float)(Math.random() * Math.PI * 2.0);
				}
			}
		}

		oRoll = roll;

		BlockPos pos = BlockPos.containing(x, y, z);
		FluidState fluidState = level.getFluidState(pos);
		if (fluidState.is(FluidTags.WATER))
		{
			if (gravity > 0)
			{
				y = pos.getY() + fluidState.getHeight(level, pos);
				if (ParticularConfig.COMMON.fallingLeavesSpawnRipples.get())
				{
					level.addParticle(Particles.WATER_RIPPLE(), x, y, z, 0, 0, 0);
				}
			}

			// Float on top of water
			yd = 0;
			gravity = 0;
			// investigate this
		}
		else if (age >= fadeInDuration)
		{
			gravity = gravityFactor;
			if (!onGround)
			{
				roll = getAngle();
			}
		}
	}

	@Override
	protected SingleQuadParticle.Layer getLayer()
	{
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public void extract(QuadParticleRenderState renderState, Camera camera, float tickDelta)
	{
		float ageDelta = Mth.lerpInt(tickDelta, age, age + 1);
		if (age <= fadeInDuration)
		{
			setAlpha(ageDelta / (float)fadeInDuration);
		}
		else if (age > lifetime - fadeOutDuration)
		{
			setAlpha(Math.max(0.0f, (lifetime - ageDelta) / (float)fadeOutDuration));
		}
		else
		{
			setAlpha(1);
		}

		Vec3 vec3d = camera.getPosition();
		float f = (float)(Mth.lerp(tickDelta, xo, x) - vec3d.x());
		float g = (float)(Mth.lerp(tickDelta, yo, y) - vec3d.y());
		float h = (float)(Mth.lerp(tickDelta, zo, z) - vec3d.z());

		if (!expiring || !ParticularConfig.COMMON.fallingLeavesLayFlatOnGround.get())
		{
			Quaternionf quaternionf = new Quaternionf(camera.rotation());
			if (roll != 0.0F)
			{
				quaternionf.rotateZ(Mth.lerp(tickDelta, oRoll, roll));
			}
			extractRotatedQuad(renderState, quaternionf, f, g, h, tickDelta);
		}
		else
		{
			Quaternionf quaternionf = new Quaternionf();
			quaternionf.rotateY(Mth.lerp(tickDelta, oRoll, roll));
			extractRotatedQuad(renderState, quaternionf, f, g, h, tickDelta);
		}
	}

	@Override
	protected void extractRotatedQuad(QuadParticleRenderState renderState, Quaternionf rotation, float x, float y, float z, float partialTick)
	{
		float u0 = getU0();
		float u1 = getU1();
		if (flippedSprite)
		{
			float temp = u0;
			u0 = u1;
			u1 = temp;
		}

		renderState.add(
			getLayer(),
			x, y, z,
			rotation.x, rotation.y, rotation.z, rotation.w,
			getQuadSize(partialTick),
			u0, u1, getV0(), getV1(),
			ARGB.colorFromFloat(alpha, rCol, gCol, bCol),
			getLightColor(partialTick)
		);
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
			return new LeafParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}