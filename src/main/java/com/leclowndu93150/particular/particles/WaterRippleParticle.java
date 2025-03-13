package com.leclowndu93150.particular.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

public class WaterRippleParticle extends TextureSheetParticle
{
	protected final SpriteSet provider;

	protected WaterRippleParticle(ClientLevel world, double x, double y, double z, SpriteSet provider)
	{
		super(world, x, y, z);
		lifetime = 7;
		alpha = 0.2f;
		quadSize = 0.25f;
		this.provider = provider;
		setSpriteFromAge(provider);
	}

	@Override
	public void tick()
	{
		xo = x;
		yo = y;
		zo = z;
		if (age++ >= lifetime)
		{
			remove();
		}
		else
		{
			setSpriteFromAge(provider);
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

		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0f), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};
		float j = getQuadSize(tickDelta);

		for (int k = 0; k < 4; ++k)
		{
			Vector3f vector3f2 = vector3fs[k];
			vector3f2.mul(j);
			vector3f2.add(f, g, h);
		}

		float l = getU0();
		float m = getU1();
		float n = getV0();
		float o = getV1();
		int p = getLightColor(tickDelta);
		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(m, o).color(rCol, gCol, bCol, alpha).uv2(p);
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(m, n).color(rCol, gCol, bCol, alpha).uv2(p);
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(l, n).color(rCol, gCol, bCol, alpha).uv2(p);
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(l, o).color(rCol, gCol, bCol, alpha).uv2(p);
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
			return new WaterRippleParticle(world, x, y, z, provider);
		}
	}
}