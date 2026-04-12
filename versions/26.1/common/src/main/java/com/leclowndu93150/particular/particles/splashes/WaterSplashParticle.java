package com.leclowndu93150.particular.particles.splashes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class WaterSplashParticle extends Particle {
    public static final ParticleRenderType WATER_SPLASH_TYPE = new ParticleRenderType("particular:water_splash");

    protected final SpriteSet provider;
    private final float width;
    private final float height;
    protected static final Color LAVA_COLOR = new Color(207, 92, 15);
    protected Color color;
    protected boolean colored = true;
    protected boolean isLava = false;

    WaterSplashParticle(ClientLevel clientWorld, double x, double y, double z, float width, float height, SpriteSet provider) {
        super(clientWorld, x, y, z);
        gravity = 0;
        lifetime = 18;
        this.width = width;
        this.height = height;
        this.provider = provider;
        color = new Color(BiomeColors.getAverageWaterColor(clientWorld, BlockPos.containing(x, y, z)));
    }

    WaterSplashParticle(ClientLevel clientWorld, double x, double y, double z, float width, float height, SpriteSet provider, boolean lava) {
        super(clientWorld, x, y, z);
        gravity = 0;
        lifetime = 18;
        this.width = width;
        this.height = height;
        this.provider = provider;
        this.isLava = lava;
        color = lava ? LAVA_COLOR : new Color(BiomeColors.getAverageWaterColor(clientWorld, BlockPos.containing(x, y, z)));
    }

    @Override
    public ParticleRenderType getGroup() {
        return WATER_SPLASH_TYPE;
    }

    @Override
    public void tick() {
        super.tick();
        var fluid = level.getFluidState(BlockPos.containing(x, y, z));
        if (isLava ? !fluid.is(FluidTags.LAVA) : !fluid.is(FluidTags.WATER)) {
            this.remove();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public Factory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double g, double h, double i, RandomSource random) {
            return new WaterSplashParticle(world, x, y, z, (float) g, (float) h, provider, i > 0);
        }
    }

    public static class WaterSplashParticleGroup extends ParticleGroup<WaterSplashParticle> {
        public WaterSplashParticleGroup(ParticleEngine engine) {
            super(engine);
        }

        @Override
        public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTick) {
            List<WaterSplashRenderData> renderDataList = new ArrayList<>();

            for (WaterSplashParticle particle : this.particles) {
                if (!particle.isAlive()) continue;

                var sprite = particle.provider.get(particle.age, particle.lifetime);
                float uvWidth = sprite.getU1() - sprite.getU0();
                float unit = uvWidth / (sprite.contents().width() / 2f);
                float u0 = sprite.getU0() + unit;
                float u1 = sprite.getU1() - unit;
                float v0 = sprite.getV0();
                float v1 = sprite.getV1();

                float rCol = particle.colored ? particle.color.getRed() / 255f : 1f;
                float gCol = particle.colored ? particle.color.getGreen() / 255f : 1f;
                float bCol = particle.colored ? particle.color.getBlue() / 255f : 1f;

                Vec3 vec3 = camera.position();
                float camX = (float)(Mth.lerp(partialTick, particle.xo, particle.x) - vec3.x());
                float camY = (float)(Mth.lerp(partialTick, particle.yo, particle.y) - vec3.y());
                float camZ = (float)(Mth.lerp(partialTick, particle.zo, particle.z) - vec3.z());

                float ageDelta = Mth.lerp(partialTick, particle.age - 1, (float)particle.age);
                float progress = ageDelta / (float)particle.lifetime;
                float scale = particle.width * (0.8f + 0.2f * progress);

                Vector3f[] corners = new Vector3f[]{
                        new Vector3f(-1.0F, 0.0F, -1.0f),
                        new Vector3f(-1.0F, 0.0F, 1.0F),
                        new Vector3f(1.0F, 0.0F, 1.0F),
                        new Vector3f(1.0F, 0.0F, -1.0F)
                };

                for (int i = 0; i < 4; ++i) {
                    corners[i].mul(scale);
                    corners[i].add(camX, camY, camZ);
                }

                int light = particle.getLightCoords(partialTick);
                int argb = ARGB.colorFromFloat(1.0f, rCol, gCol, bCol);

                renderDataList.add(new WaterSplashRenderData(
                        corners, camY, particle.height, u0, u1, v0, v1, argb, light
                ));
            }

            return new WaterSplashGroupRenderState(renderDataList);
        }
    }

    private record WaterSplashRenderData(
            Vector3f[] corners,
            float baseY,
            float height,
            float u0, float u1, float v0, float v1,
            int color,
            int light
    ) {}

    private static class WaterSplashGroupRenderState implements ParticleGroupRenderState {
        private final List<WaterSplashRenderData> renderDataList;

        public WaterSplashGroupRenderState(List<WaterSplashRenderData> renderDataList) {
            this.renderDataList = renderDataList;
        }

        @Override
        public void submit(SubmitNodeCollector collector, CameraRenderState cameraState) {
            if (renderDataList.isEmpty()) return;

            PoseStack poseStack = new PoseStack();

            collector.submitCustomGeometry(
                    poseStack,
                    RenderTypes.entityTranslucent(TextureAtlas.LOCATION_PARTICLES),
                    (pose, consumer) -> {
                        for (WaterSplashRenderData data : renderDataList) {
                            renderSide(consumer, data.corners, 0, 1, data.baseY, data.height, data.u0, data.u1, data.v0, data.v1, data.color, data.light);
                            renderSide(consumer, data.corners, 1, 2, data.baseY, data.height, data.u0, data.u1, data.v0, data.v1, data.color, data.light);
                            renderSide(consumer, data.corners, 2, 3, data.baseY, data.height, data.u0, data.u1, data.v0, data.v1, data.color, data.light);
                            renderSide(consumer, data.corners, 3, 0, data.baseY, data.height, data.u0, data.u1, data.v0, data.v1, data.color, data.light);
                        }
                    }
            );
        }

        private void renderSide(VertexConsumer consumer, Vector3f[] corners, int a, int b, float baseY, float height,
                                float u0, float u1, float v0, float v1, int color, int light) {
            Vector3f cornerA = corners[a];
            Vector3f cornerB = corners[b];

            float bottomAX = cornerA.x;
            float bottomAZ = cornerA.z;

            float bottomBX = cornerB.x;
            float bottomBZ = cornerB.z;

            float topAX = cornerA.x;
            float topAZ = cornerA.z;

            float topBX = cornerB.x;
            float topBZ = cornerB.z;

            float dx = bottomBX - bottomAX;
            float dz = bottomBZ - bottomAZ;
            float normalX = -dz;
            float normalY = 0f;
            float normalZ = dx;
            float length = (float)Math.sqrt(normalX * normalX + normalZ * normalZ);
            if (length > 0) {
                normalX /= length;
                normalZ /= length;
            }

            int noOverlay = OverlayTexture.NO_OVERLAY;

            consumer.addVertex(bottomAX, baseY, bottomAZ).setUv(u0, v1).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(normalX, normalY, normalZ);
            consumer.addVertex(bottomBX, baseY, bottomBZ).setUv(u1, v1).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(normalX, normalY, normalZ);
            consumer.addVertex(topBX, baseY + height, topBZ).setUv(u1, v0).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(normalX, normalY, normalZ);
            consumer.addVertex(topAX, baseY + height, topAZ).setUv(u0, v0).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(normalX, normalY, normalZ);

            consumer.addVertex(bottomBX, baseY, bottomBZ).setUv(u0, v1).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(-normalX, normalY, -normalZ);
            consumer.addVertex(bottomAX, baseY, bottomAZ).setUv(u1, v1).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(-normalX, normalY, -normalZ);
            consumer.addVertex(topAX, baseY + height, topAZ).setUv(u1, v0).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(-normalX, normalY, -normalZ);
            consumer.addVertex(topBX, baseY + height, topBZ).setUv(u0, v0).setColor(color).setOverlay(noOverlay).setLight(light).setNormal(-normalX, normalY, -normalZ);
        }
    }
}