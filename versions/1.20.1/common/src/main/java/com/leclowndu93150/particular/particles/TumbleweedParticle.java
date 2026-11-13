package com.leclowndu93150.particular.particles;

import com.leclowndu93150.particular.ParticularConfig;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TumbleweedParticle extends TextureSheetParticle
{
	public static final int RADIUS = 25;

	private static final int LIFETIME = RADIUS * 10;
	private static final int FADE_IN_TICKS = 10;
	private static final float FADE_OUT_STEP = 0.05f;
	private static final float TEMPERATURE_TOLERANCE = 0.4f;
	private static final double LAUNCH_SPEED = 0.3;
	private static final double WIND_SPEED = 0.2;

	private static int alive;

	private final BlockPos.MutableBlockPos pos;
	private final float temperature;
	private boolean fadingOut;

	public TumbleweedParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue)
	{
		super(level, x, y, z);
		setSize(0.01f, 0.01f);
		setSprite(sprite);
		setColor(red, green, blue);
		lifetime = LIFETIME;
		alpha = 0f;
		quadSize = 0.5f;
		gravity = ParticularConfig.COMMON.tumbleweedGravity.get().floatValue();
		xd = LAUNCH_SPEED;
		yd = 0.1;
		zd = LAUNCH_SPEED;
		pos = new BlockPos.MutableBlockPos(x, y, z);
		temperature = level.getBiome(pos).value().getBaseTemperature();
		alive++;
	}

	public static int alive()
	{
		return alive;
	}

	public static void reset()
	{
		alive = 0;
	}

	@Override
	public void tick()
	{
		super.tick();
		if (!isAlive()) { return; }

		pos.set(x, y - 0.2, z);

		Entity camera = Minecraft.getInstance().getCameraEntity();
		if (camera == null || camera.distanceToSqr(x, y, z) > RADIUS * RADIUS)
		{
			fadingOut = true;
		}

		if (fadingOut)
		{
			alpha -= FADE_OUT_STEP;
			if (alpha < 0.01f)
			{
				remove();
				return;
			}
		}
		else
		{
			alpha = Math.min(1f, (float) age / FADE_IN_TICKS);
			if (age % 10 == 0 && Mth.abs(level.getBiome(pos).value().getBaseTemperature() - temperature) > TEMPERATURE_TOLERANCE)
			{
				fadingOut = true;
			}
		}

		if (x == xo || z == zo)
		{
			remove();
			return;
		}

		if (!level.getFluidState(pos).isEmpty())
		{
			fadingOut = true;
			gravity = 0f;
		}
		else
		{
			xd = WIND_SPEED;
			zd = WIND_SPEED;
		}

		oRoll = roll;
		roll += ParticularConfig.COMMON.tumbleweedRotation.get().floatValue();
		if (onGround)
		{
			yd = ParticularConfig.COMMON.tumbleweedBounciness.get();
		}
	}

	@Override
	public void remove()
	{
		if (isAlive() && alive > 0)
		{
			alive--;
		}
		super.remove();
	}

	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.TERRAIN_SHEET;
	}

	@Override
	public void render(VertexConsumer buffer, Camera camera, float partialTicks)
	{
		Vector3f cameraPos = camera.getPosition().toVector3f();
		float renderX = (float) (Mth.lerp(partialTicks, xo, x) - cameraPos.x);
		float renderY = (float) (Mth.lerp(partialTicks, yo, y) - cameraPos.y);
		float renderZ = (float) (Mth.lerp(partialTicks, zo, z) - cameraPos.z);
		Vector3f toCamera = new Vector3f(renderX, renderY, renderZ);

		Quaternionf heading = new Quaternionf().rotateY((float) Math.atan2(xd, zd));
		float spin = Mth.lerp(partialTicks, oRoll, roll);

		Quaternionf first = new Quaternionf(new AxisAngle4f(0f, 0f, 1f, 0f)).mul(heading).rotateX(spin);
		Quaternionf second = new Quaternionf(new AxisAngle4f(Mth.HALF_PI, 0f, 1f, 0f)).mul(heading).rotateZ(spin);
		quad(buffer, faceCamera(first, toCamera), renderX, renderY, renderZ, partialTicks);
		quad(buffer, faceCamera(second, toCamera), renderX, renderY, renderZ, partialTicks);
	}

	private void quad(VertexConsumer buffer, Quaternionf rotation, float renderX, float renderY, float renderZ, float partialTicks)
	{
		float size = getQuadSize(partialTicks);
		int light = getLightColor(partialTicks);
		vertex(buffer, rotation, renderX, renderY, renderZ, 1f, -1f, size, getU1(), getV1(), light);
		vertex(buffer, rotation, renderX, renderY, renderZ, 1f, 1f, size, getU1(), getV0(), light);
		vertex(buffer, rotation, renderX, renderY, renderZ, -1f, 1f, size, getU0(), getV0(), light);
		vertex(buffer, rotation, renderX, renderY, renderZ, -1f, -1f, size, getU0(), getV1(), light);
	}

	private void vertex(VertexConsumer buffer, Quaternionf rotation, float renderX, float renderY, float renderZ, float cornerX, float cornerY, float size, float u, float v, int light)
	{
		Vector3f corner = new Vector3f(cornerX, cornerY, 0f).rotate(rotation).mul(size).add(renderX, renderY, renderZ);
		buffer.vertex(corner.x(), corner.y(), corner.z()).uv(u, v).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
	}

	private static Quaternionf faceCamera(Quaternionf rotation, Vector3f toCamera)
	{
		Vector3f normal = new Vector3f(0f, 0f, 1f).rotate(rotation).normalize();
		return normal.dot(toCamera) > 0f ? rotation.mul(Axis.YP.rotation(Mth.PI)) : rotation;
	}
}
