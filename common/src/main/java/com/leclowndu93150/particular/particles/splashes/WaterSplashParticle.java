package com.leclowndu93150.particular.particles.splashes;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import java.awt.Color;

public class WaterSplashParticle extends SingleQuadParticle {
    protected final SpriteSet provider;
    private final float width;
    private final float height;
    private final Color color;
    protected boolean colored = true;

    WaterSplashParticle(ClientLevel clientWorld, double x, double y, double z, float width, float height, SpriteSet provider) {
        super(clientWorld, x, y, z, provider.first());
        gravity = 0;
        lifetime = 18;
        this.width = width;
        this.height = height;
        this.provider = provider;
        setSpriteFromAge(provider);

        color = new Color(BiomeColors.getAverageWaterColor(clientWorld, BlockPos.containing(x, y, z)));

        System.out.println("WaterSplashParticle created: width=" + width + ", height=" + height);
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(provider);

        if (!level.getFluidState(BlockPos.containing(x, y, z)).is(FluidTags.WATER)) {
            this.remove();
        }
    }

    @Override
    public void extract(QuadParticleRenderState renderState, Camera camera, float partialTick) {
        if (colored) {
            this.rCol = color.getRed() / 255f;
            this.gCol = color.getGreen() / 255f;
            this.bCol = color.getBlue() / 255f;
        } else {
            this.rCol = 1f;
            this.gCol = 1f;
            this.bCol = 1f;
        }

        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float g = (float)(Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float h = (float)(Mth.lerp(partialTick, this.zo, this.z) - vec3.z());

        float ageDelta = Mth.lerp(partialTick, age - 1, (float)age);
        float progress = ageDelta / (float)lifetime;
        float scale = width * (0.8f + 0.2f * progress);

        Vector3f[] corners = new Vector3f[]{
                new Vector3f(-1.0F, 0.0F, -1.0f),
                new Vector3f(-1.0F, 0.0F, 1.0F),
                new Vector3f(1.0F, 0.0F, 1.0F),
                new Vector3f(1.0F, 0.0F, -1.0F)
        };

        for (int i = 0; i < 4; ++i) {
            Vector3f corner = corners[i];
            corner.mul(scale);
            corner.add(f, g, h);
        }

        TextureAtlasSprite sprite = this.sprite;
        float uvWidth = sprite.getU1() - sprite.getU0();
        float unit = uvWidth / (sprite.contents().width() / 2f);

        float u0 = sprite.getU0() + unit;
        float u1 = sprite.getU1() - unit;
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        int light = this.getLightColor(partialTick);
        int argb = net.minecraft.util.ARGB.colorFromFloat(1.0f, this.rCol, this.gCol, this.bCol);

        submitSide(renderState, corners, 0, 1, u0, u1, v0, v1, argb, light);
        submitSide(renderState, corners, 1, 2, u0, u1, v0, v1, argb, light);
        submitSide(renderState, corners, 2, 3, u0, u1, v0, v1, argb, light);
        submitSide(renderState, corners, 3, 0, u0, u1, v0, v1, argb, light);
    }

    private void submitSide(QuadParticleRenderState renderState, Vector3f[] corners, int a, int b,
                            float u0, float u1, float v0, float v1, int color, int light) {
        Vector3f cornerA = corners[a];
        Vector3f cornerB = corners[b];

        float midX = (cornerA.x + cornerB.x) / 2f;
        float midY = (cornerA.y + cornerB.y) / 2f + height / 2f;
        float midZ = (cornerA.z + cornerB.z) / 2f;

        // Calculate the width of this side (distance between corners)
        float sideWidth = (float)Math.sqrt(
                Math.pow(cornerB.x - cornerA.x, 2) +
                        Math.pow(cornerB.z - cornerA.z, 2)
        );

        float dx = cornerB.x - cornerA.x;
        float dz = cornerB.z - cornerA.z;
        float angle = (float)Math.atan2(dx, dz) + (float)Math.PI / 2f;

        Quaternionf rotation = new Quaternionf();
        rotation.rotateY(angle);

        // Use the side width divided by 2 as the quad size (radius, not diameter)
        // This makes each quad exactly as wide as the space between corners
        float quadSize = sideWidth / 2f;

        // Submit front face
        renderState.add(
                this.getLayer(),
                midX, midY, midZ,
                rotation.x, rotation.y, rotation.z, rotation.w,
                quadSize,
                u0, u1, v0, v1,
                color,
                light
        );

        // Submit back face
        rotation.rotateY((float)Math.PI);
        renderState.add(
                this.getLayer(),
                midX, midY, midZ,
                rotation.x, rotation.y, rotation.z, rotation.w,
                quadSize,
                u1, u0, v0, v1,
                color,
                light
        );
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public Factory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double g, double h, double i, RandomSource random) {
            return new WaterSplashParticle(world, x, y, z, (float) g, (float) h, provider);
        }
    }
}