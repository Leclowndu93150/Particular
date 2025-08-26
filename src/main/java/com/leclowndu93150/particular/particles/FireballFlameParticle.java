package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireballFlameParticle extends TextureSheetParticle
{
    private final float initialScale;
    private final SpriteSet sprites;
    
    protected FireballFlameParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites)
    {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        
        this.sprites = sprites;
        this.friction = 0.96f;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.xd += (this.random.nextFloat() - 0.5f) * 0.02f;
        this.yd += (this.random.nextFloat() - 0.5f) * 0.02f;
        this.zd += (this.random.nextFloat() - 0.5f) * 0.02f;

        this.lifetime = 15 + this.random.nextInt(10);

        this.initialScale = 0.1f + this.random.nextFloat() * 0.3f;
        this.quadSize = this.initialScale;

        this.setColor(1.0f, 0.8f + this.random.nextFloat() * 0.2f, 0.1f + this.random.nextFloat() * 0.2f);

        this.setSpriteFromAge(sprites);
        this.hasPhysics = false;
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);

            float ageProgress = (float) this.age / (float) this.lifetime;

            float scaleMultiplier;
            if (ageProgress < 0.3f) {
                scaleMultiplier = ageProgress / 0.3f;
            } else if (ageProgress < 0.7f) {
                scaleMultiplier = 1.0f;
            } else {
                scaleMultiplier = 1.0f - ((ageProgress - 0.7f) / 0.3f);
            }
            
            this.quadSize = this.initialScale * scaleMultiplier * (1.5f + 0.5f * Mth.sin(this.age * 0.3f));

            if (ageProgress < 0.5f) {
                float factor = ageProgress * 2.0f;
                this.rCol = 1.0f;
                this.gCol = 0.8f - factor * 0.4f;
                this.bCol = 0.1f - factor * 0.1f;
            } else {
                float factor = (ageProgress - 0.5f) * 2.0f;
                this.rCol = 1.0f - factor * 0.3f;
                this.gCol = 0.4f - factor * 0.4f;
                this.bCol = 0.0f;
            }

            if (ageProgress > 0.7f) {
                this.alpha = 1.0f - ((ageProgress - 0.7f) / 0.3f);
            } else {
                this.alpha = 1.0f;
            }

            this.yd += 0.002f;

            this.xd += (this.random.nextFloat() - 0.5f) * 0.001f;
            this.zd += (this.random.nextFloat() - 0.5f) * 0.001f;

            this.move(this.xd, this.yd, this.zd);

            this.xd *= this.friction;
            this.yd *= 0.98f;
            this.zd *= this.friction;

            this.yd -= 0.001f;
        }
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider)
        {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xd, double yd, double zd)
        {
            return new FireballFlameParticle(level, x, y, z, xd, yd, zd, this.spriteProvider);
        }
    }
}