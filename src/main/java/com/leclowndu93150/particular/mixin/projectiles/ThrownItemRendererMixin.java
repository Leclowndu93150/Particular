package com.leclowndu93150.particular.mixin.projectiles;

import com.leclowndu93150.particular.ParticularConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import com.leclowndu93150.particular.Particles;
import java.awt.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemRenderer.class)
public class ThrownItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), remap = false, cancellable = true)
    private void onRenderHead(Entity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Fireball fireball && ParticularConfig.fireballFlames()) {
            if(entity instanceof SmallFireball) {
                ci.cancel();
            }
            createFireballParticleEffect(fireball, partialTicks);
        }
    }
    
    private void createFireballParticleEffect(Fireball fireball, float deltaTime) {
        float posX = (float) Mth.lerp(deltaTime, fireball.xo, fireball.getX());
        float posY = (float) Mth.lerp(deltaTime, fireball.yo, fireball.getY());
        float posZ = (float) Mth.lerp(deltaTime, fireball.zo, fireball.getZ());
        
        float intensityFactor = 1.0f;
        int particleQuantity = 3;
        
        if (fireball instanceof SmallFireball) {
            intensityFactor = 0.7f;
            particleQuantity = 2;
        } else if (fireball instanceof LargeFireball) {
            intensityFactor = 1.8f;
            particleQuantity = 5;
        }

        spawnCoreFlameParticles(fireball, posX, posY, posZ, intensityFactor, particleQuantity);

        Vec3 motion = fireball.getDeltaMovement();
        if (motion.length() > 0.1) {
            spawnTrailFlameParticles(fireball, posX, posY, posZ, motion, intensityFactor);
        }
    }
    
    private void spawnCoreFlameParticles(Fireball fireball, float x, float y, float z, float scale, int count) {
        for (int i = 0; i < count; i++) {
            WorldParticleBuilder.create(new WorldParticleOptions(Particles.FIREBALL_FLAME_LODESTONE.get()))
                .setSpinData(SpinParticleData.create((float)(fireball.level().random.nextGaussian() * 0.25f)).build())
                .setScaleData(GenericParticleData.create(scale * 0.2f, 0f).setEasing(Easing.EXPO_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.95f, 0f).setEasing(Easing.QUAD_OUT).build())
                .setColorData(
                    ColorParticleData.create(new Color(0xFF8800), new Color(0xDD2200))
                        .setEasing(Easing.SINE_IN_OUT)
                        .build()
                )
                .enableNoClip()
                .setLifetime(20 + fireball.level().random.nextInt(10))
                .spawn(
                    fireball.level(), 
                    x + fireball.level().random.nextGaussian() * 0.12, 
                    y + (fireball.getBbHeight() * 0.5) + fireball.level().random.nextGaussian() * 0.12, 
                    z + fireball.level().random.nextGaussian() * 0.12
                );
        }
    }
    
    private void spawnTrailFlameParticles(Fireball fireball, float x, float y, float z, Vec3 velocity, float scale) {
        for (int i = 0; i < 2; i++) {
            double trailDistance = (i + 1) * 0.3;
            double trailX = x - velocity.x * trailDistance;
            double trailY = y - velocity.y * trailDistance;
            double trailZ = z - velocity.z * trailDistance;
            
            float trailScale = scale * (0.6f - i * 0.2f);
            
            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setSpinData(SpinParticleData.create((float)(fireball.level().random.nextGaussian() * 0.15f)).build())
                .setScaleData(GenericParticleData.create(trailScale, 0f).setEasing(Easing.CIRC_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.7f - i * 0.2f, 0f).setEasing(Easing.EXPO_OUT).build())
                .setColorData(
                    ColorParticleData.create(new Color(0xFF4400), new Color(0x880000))
                        .setEasing(Easing.QUAD_IN)
                        .build()
                )
                .enableNoClip()
                .setLifetime(15 + fireball.level().random.nextInt(10))
                .spawn(
                    fireball.level(),
                    trailX + fireball.level().random.nextGaussian() * 0.1,
                    trailY + fireball.level().random.nextGaussian() * 0.1,
                    trailZ + fireball.level().random.nextGaussian() * 0.1
                );
        }
    }
}