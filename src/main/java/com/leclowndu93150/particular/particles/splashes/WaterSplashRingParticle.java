package com.leclowndu93150.particular.particles.splashes;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class WaterSplashRingParticle extends TextureSheetParticle
{
	protected final SpriteSet provider;
	private final float width;

	WaterSplashRingParticle(ClientLevel clientWorld, double x, double y, double z, float width, SpriteSet provider)
	{
		super(clientWorld, x, y, z);
		gravity = 0;
		lifetime = 18;
		this.width = width;
		this.provider = provider;
		setSpriteFromAge(provider);
	}

	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick()
	{
		super.tick();

		setSpriteFromAge(provider);

		if (!level.getFluidState(BlockPos.containing(x, y, z)).is(FluidTags.WATER))
		{
			this.remove();
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		Vec3 vec3d = camera.getPosition();
		float f = (float) (Mth.lerp(tickDelta, xo, x) - vec3d.x());
		float g = (float) (Mth.lerp(tickDelta, yo, y) - vec3d.y());
		float h = (float) (Mth.lerp(tickDelta, zo, z) - vec3d.z());

		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0f), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};
		float ageDelta = Mth.lerp(tickDelta, age - 1, (float)age);
		float progress = ageDelta / (float)lifetime;
		float scale = width * (0.8f + 0.2f * progress);

		for (int k = 0; k < 4; ++k)
		{
			Vector3f vector3f2 = vector3fs[k];
			vector3f2.mul(scale);
			vector3f2.add(f, g, h);
		}

		float l = getU0();
		float m = getU1();
		float n = getV0();
		float o = getV1();
		int p = getLightColor(tickDelta);
		vertexConsumer.addVertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).setUv(m, o).setColor(rCol, gCol, bCol, alpha).setLight(p);
		vertexConsumer.addVertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).setUv(m, n).setColor(rCol, gCol, bCol, alpha).setLight(p);
		vertexConsumer.addVertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).setUv(l, n).setColor(rCol, gCol, bCol, alpha).setLight(p);
		vertexConsumer.addVertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).setUv(l, o).setColor(rCol, gCol, bCol, alpha).setLight(p);
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
		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double x, double y, double z, double g, double h, double i)
		{
			return new WaterSplashRingParticle(clientWorld, x, y, z, (float) g, provider);
		}
	}
}