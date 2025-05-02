package com.leclowndu93150.particular.particles.leaves;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class LeafParticle extends TextureSheetParticle
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
		super(world, x, y, z, r, g, b);
		pickSprite(provider);

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
					level.addParticle(Particles.WATER_RIPPLE.get(), x, y, z, 0, 0, 0);
				}
			}

			// Float on top of water
			yd = 0;
			gravity = 0;
		}
		else if (age >= fadeInDuration)
		{
			gravity = gravityFactor;
			/*
			if (gravity > 0) {
				yd -= gravity;
			}
			 */

			if (!onGround)
			{
				roll = getAngle();
			}
		}
	}

	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		Vec3 vec3d = camera.getPosition();
		float f = (float)(Mth.lerp(tickDelta, xo, x) - vec3d.x());
		float g = (float)(Mth.lerp(tickDelta, yo, y) - vec3d.y());
		float h = (float)(Mth.lerp(tickDelta, zo, z) - vec3d.z());

		Vector3f[] vector3fs;
		float j = getQuadSize(tickDelta);

		if (!expiring || !ParticularConfig.COMMON.fallingLeavesLayFlatOnGround.get())
		{
			vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};

			Quaternionf quaternionf;
			if (roll == 0.0F)
			{
				quaternionf = camera.rotation();
			}
			else
			{
				quaternionf = new Quaternionf(camera.rotation());
				quaternionf.rotateZ(Mth.lerp(tickDelta, oRoll, roll));
			}

			for (int k = 0; k < 4; ++k)
			{
				Vector3f vector3f = vector3fs[k];
				vector3f.rotate(quaternionf);
				vector3f.mul(j);
				vector3f.add(f, g, h);
			}
		}
		else
		{
			vector3fs = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0f), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};

			for (int k = 0; k < 4; ++k)
			{
				Vector3f vector3f = vector3fs[k];
				vector3f.rotateAxis(roll, 0, 1, 0);
				vector3f.mul(j);
				vector3f.add(f, g, h);
			}
		}

		float ageDelta = Mth.lerpInt(tickDelta, age, age + 1);
		if (age <= fadeInDuration)
		{
			alpha = ageDelta / (float)fadeInDuration;
		}
		else if (age > lifetime - fadeOutDuration)
		{
			alpha = Math.max(0.0f, (lifetime - ageDelta) / (float)fadeOutDuration);
		}
		else
		{
			alpha = 1;
		}

		float l = getU0();
		float m = getU1();
		float n = getV0();
		float o = getV1();
		int p = getLightColor(tickDelta);
		if (flippedSprite)
		{
			float temp = l;
			l = m;
			m = temp;
		}

		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(l, o).color(rCol, gCol, bCol, alpha).uv2(p).endVertex();
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(l, n).color(rCol, gCol, bCol, alpha).uv2(p).endVertex();
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(m, n).color(rCol, gCol, bCol, alpha).uv2(p).endVertex();
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(m, o).color(rCol, gCol, bCol, alpha).uv2(p).endVertex();
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
			return new LeafParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}