package com.leclowndu93150.particular.particles.splashes;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.VertexConsumer;

import javax.annotation.Nullable;
import java.awt.*;

public class WaterSplashParticle extends TextureSheetParticle
{
	protected final SpriteSet provider;
	private final float width;
	private final float height;
	private final Color color;
	private final float unit;
	protected boolean colored = true;

	WaterSplashParticle(ClientLevel clientWorld, double x, double y, double z, float width, float height, SpriteSet provider)
	{
		super(clientWorld, x, y, z);
		gravity = 0;
		lifetime = 18;
		this.width = width;
		this.height = height;
		this.provider = provider;
		setSpriteFromAge(provider);

		color = new Color(BiomeColors.getAverageWaterColor(clientWorld, BlockPos.containing(x, y, z)));
		unit = 2f / Minecraft.getInstance().particleEngine.textureAtlas.getWidth();
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
		float f = (float)(Mth.lerp(tickDelta, xo, x) - vec3d.x());
		float g = (float)(Mth.lerp(tickDelta, yo, y) - vec3d.y());
		float h = (float)(Mth.lerp(tickDelta, zo, z) - vec3d.z());

		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0f), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};
		float ageDelta = Mth.lerp(tickDelta, age - 1, (float)age);
		float progress = ageDelta / (float)lifetime;
		float scale = width * (0.8f + 0.2f * progress);

		for (int i = 0; i < 4; ++i)
		{
			Vector3f vector3f2 = vector3fs[i];
			vector3f2.mul(scale);
			vector3f2.add(f, g, h);
		}

		float l = getU0() + unit;
		float m = getU1() - unit;
		float n = getV0();
		float o = getV1();
		int light = getLightColor(tickDelta);
		int color = colored ? this.color.getRGB() : Color.white.getRGB();
		renderSide(vertexConsumer, vector3fs, 0, 1, height, l, m, n, o, light, color);
		renderSide(vertexConsumer, vector3fs, 1, 2, height, l, m, n, o, light, color);
		renderSide(vertexConsumer, vector3fs, 2, 3, height, l, m, n, o, light, color);
		renderSide(vertexConsumer, vector3fs, 3, 0, height, l, m, n, o, light, color);
	}
	private void renderSide(VertexConsumer vertexConsumer, Vector3f[] vector3fs, int a, int b, float height, float l, float m, float n, float o, int light, int color)
	{
		vertexConsumer.vertex(vector3fs[a].x(), vector3fs[a].y(), vector3fs[a].z()).uv(l, o).color(color).uv2(light);
		vertexConsumer.vertex(vector3fs[b].x(), vector3fs[b].y(), vector3fs[b].z()).uv(m, o).color(color).uv2(light);
		vertexConsumer.vertex(vector3fs[b].x(), vector3fs[b].y() + height, vector3fs[b].z()).uv(m, n).color(color).uv2(light);
		vertexConsumer.vertex(vector3fs[a].x(), vector3fs[a].y() + height, vector3fs[a].z()).uv(l, n).color(color).uv2(light);

		vertexConsumer.vertex(vector3fs[b].x(), vector3fs[b].y(), vector3fs[b].z()).uv(m, o).color(color).uv2(light);
		vertexConsumer.vertex(vector3fs[a].x(), vector3fs[a].y(), vector3fs[a].z()).uv(l, o).color(color).uv2(light);
		vertexConsumer.vertex(vector3fs[a].x(), vector3fs[a].y() + height, vector3fs[a].z()).uv(l, n).color(color).uv2(light);
		vertexConsumer.vertex(vector3fs[b].x(), vector3fs[b].y() + height, vector3fs[b].z()).uv(m, n).color(color).uv2(light);
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
			return new WaterSplashParticle(clientWorld, x, y, z, (float) g, (float) h, provider);
		}
	}
}