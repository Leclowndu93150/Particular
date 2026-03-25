package com.leclowndu93150.particular.particles.splashes;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class WaterSplashRingParticle extends SingleQuadParticle {
    protected final SpriteSet provider;
    private final float width;

    WaterSplashRingParticle(ClientLevel clientWorld, double x, double y, double z, float width, SpriteSet provider) {
        super(clientWorld, x, y, z, provider.first());
        gravity = 0;
        lifetime = 18;
        this.width = width;
        this.provider = provider;
        setSpriteFromAge(provider);
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
        Vec3 vec3 = camera.position();
        float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - vec3.y()) + 0.01f;
        float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - vec3.z());

        float ageDelta = Mth.lerp(partialTick, age - 1, (float)age);
        float progress = ageDelta / (float)lifetime;
        float scale = width * (0.8f + 0.2f * progress);

        Quaternionf rotationTop = new Quaternionf().rotateX((float)Math.PI / 2.0f);
        Quaternionf rotationBottom = new Quaternionf().rotateX(-(float)Math.PI / 2.0f);

        // Submit top-facing quad
        renderState.add(
                this.getLayer(), x, y, z,
                rotationTop.x, rotationTop.y, rotationTop.z, rotationTop.w,
                scale,
                this.getU0(), this.getU1(), this.getV0(), this.getV1(),
                ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol),
                this.getLightCoords(partialTick)
        );

        // Submit bottom-facing quad
        renderState.add(
                this.getLayer(), x, y, z,
                rotationBottom.x, rotationBottom.y, rotationBottom.z, rotationBottom.w,
                scale,
                this.getU0(), this.getU1(), this.getV0(), this.getV1(),
                ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol),
                this.getLightCoords(partialTick)
        );
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public Factory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double g, double h, double i, RandomSource random) {
            return new WaterSplashRingParticle(world, x, y, z, (float) g, provider);
        }
    }
}