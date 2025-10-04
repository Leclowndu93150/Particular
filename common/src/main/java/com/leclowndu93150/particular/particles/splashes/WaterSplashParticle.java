package com.leclowndu93150.particular.particles.splashes;

import com.leclowndu93150.particular.mixin.AccessorTextureAtlas;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.data.AtlasIds;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import java.awt.*;

public class WaterSplashParticle extends SingleQuadParticle
{
	protected final SpriteSet provider;
	private final float width;
	private final float height;
	private final Color color;
	private final float unit;
	protected boolean colored = true;

	WaterSplashParticle(ClientLevel clientWorld, double x, double y, double z, float width, float height, SpriteSet provider)
	{
		super(clientWorld, x, y, z, provider.get(0, 18));
		gravity = 0;
		lifetime = 18;
		this.width = width;
		this.height = height;
		this.provider = provider;
		setSpriteFromAge(provider);
		color = new Color(BiomeColors.getAverageWaterColor(clientWorld, BlockPos.containing(x, y, z)));
        TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES);
        unit = 2f / ((AccessorTextureAtlas) atlas).invokeGetWidth();
	}

	@Override
	protected SingleQuadParticle.Layer getLayer()
	{
		return SingleQuadParticle.Layer.TRANSLUCENT;
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
	public void extract(QuadParticleRenderState renderState, Camera camera, float tickDelta)
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
		int argbColor = colored ? ARGB.color(255, color.getRed(), color.getGreen(), color.getBlue()) : ARGB.white(1);
		renderSide(renderState, vector3fs, 0, 1, height, l, m, n, o, light, argbColor);
		renderSide(renderState, vector3fs, 1, 2, height, l, m, n, o, light, argbColor);
		renderSide(renderState, vector3fs, 2, 3, height, l, m, n, o, light, argbColor);
		renderSide(renderState, vector3fs, 3, 0, height, l, m, n, o, light, argbColor);
	}

	private void renderSide(QuadParticleRenderState renderState, Vector3f[] vector3fs, int a, int b, float height, float l, float m, float n, float o, int light, int color)
	{
		renderState.add(getLayer(), vector3fs[a].x(), vector3fs[a].y(), vector3fs[a].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
		renderState.add(getLayer(), vector3fs[b].x(), vector3fs[b].y(), vector3fs[b].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
		renderState.add(getLayer(), vector3fs[b].x(), vector3fs[b].y() + height, vector3fs[b].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
		renderState.add(getLayer(), vector3fs[a].x(), vector3fs[a].y() + height, vector3fs[a].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);

		renderState.add(getLayer(), vector3fs[b].x(), vector3fs[b].y(), vector3fs[b].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
		renderState.add(getLayer(), vector3fs[a].x(), vector3fs[a].y(), vector3fs[a].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
		renderState.add(getLayer(), vector3fs[a].x(), vector3fs[a].y() + height, vector3fs[a].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
		renderState.add(getLayer(), vector3fs[b].x(), vector3fs[b].y() + height, vector3fs[b].z(), 0, 0, 0, 1, 1, l, m, n, o, color, light);
	}

	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet provider;

		public Factory(SpriteSet provider)
		{
			this.provider = provider;
		}

		@Override
		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double x, double y, double z, double g, double h, double i, RandomSource random)
		{
			return new WaterSplashParticle(clientWorld, x, y, z, (float) g, (float) h, provider);
		}
	}
}