package com.leclowndu93150.particular.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class WaterRippleParticle extends SingleQuadParticle {
    protected final SpriteSet provider;

    protected WaterRippleParticle(ClientLevel world, double x, double y, double z, SpriteSet provider) {
        super(world, x, y, z, provider.get(0, 7));
        lifetime = 7;
        alpha = 0.2f;
        quadSize = 0.25f;
        this.provider = provider;
        setSpriteFromAge(provider);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) {
            remove();
        } else {
            setSpriteFromAge(provider);
        }
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    @Override
    public void extract(QuadParticleRenderState renderState, Camera camera, float partialTick) {
        Vec3 vec3 = camera.position();
        float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - vec3.z());

        y += 0.01f;

        // Create two rotations - one for top view, one for bottom view
        Quaternionf rotationTop = new Quaternionf();
        rotationTop.rotateX((float)Math.PI / 2.0f);

        Quaternionf rotationBottom = new Quaternionf();
        rotationBottom.rotateX(-(float)Math.PI / 2.0f);

        // Submit top-facing quad
        renderState.add(
                this.getLayer(),
                x, y, z,
                rotationTop.x, rotationTop.y, rotationTop.z, rotationTop.w,
                this.getQuadSize(partialTick),
                this.getU0(), this.getU1(),
                this.getV0(), this.getV1(),
                ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol),
                this.getLightCoords(partialTick)
        );

        // Submit bottom-facing quad (same position, opposite rotation)
        renderState.add(
                this.getLayer(),
                x, y, z,
                rotationBottom.x, rotationBottom.y, rotationBottom.z, rotationBottom.w,
                this.getQuadSize(partialTick),
                this.getU0(), this.getU1(),
                this.getV0(), this.getV1(),
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
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ, RandomSource random) {
            return new WaterRippleParticle(world, x, y, z, provider);
        }
    }
}