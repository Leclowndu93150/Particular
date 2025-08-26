package com.leclowndu93150.particular.mixin.projectiles;

import com.leclowndu93150.particular.Particles;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemRenderer.class)
public class ThrownItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), remap = false)
    private void onRenderHead(Entity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Fireball fireball && ParticularConfig.fireballFlames()) {
            spawnFireballFlameTrail(fireball, partialTicks);
        }
    }
    
    private void spawnFireballFlameTrail(Fireball fireball, float partialTicks) {
        double x = Mth.lerp(partialTicks, fireball.xo, fireball.getX());
        double y = Mth.lerp(partialTicks, fireball.yo, fireball.getY());
        double z = Mth.lerp(partialTicks, fireball.zo, fireball.getZ());

        Vec3 velocity = fireball.getDeltaMovement();

        float sizeMultiplier = 1.0f;
        int particleCount = 3;
        
        if (fireball instanceof SmallFireball) {
            sizeMultiplier = 0.6f;
            particleCount = 2;
        } else if (fireball instanceof LargeFireball) {
            sizeMultiplier = 1.5f;
            particleCount = 5;
        }

        for (int i = 0; i < particleCount; i++) {
            double trailX = x - velocity.x * (0.3 + fireball.level().random.nextDouble() * 0.4);
            double trailY = y - velocity.y * (0.3 + fireball.level().random.nextDouble() * 0.4);
            double trailZ = z - velocity.z * (0.3 + fireball.level().random.nextDouble() * 0.4);

            trailX += (fireball.level().random.nextGaussian() * 0.1) * sizeMultiplier;
            trailY += (fireball.level().random.nextGaussian() * 0.1) * sizeMultiplier;
            trailZ += (fireball.level().random.nextGaussian() * 0.1) * sizeMultiplier;

            double particleVelX = velocity.x * 0.3 + (fireball.level().random.nextGaussian() * 0.02);
            double particleVelY = velocity.y * 0.3 + (fireball.level().random.nextGaussian() * 0.02) + 0.01; // Slight upward
            double particleVelZ = velocity.z * 0.3 + (fireball.level().random.nextGaussian() * 0.02);

            fireball.level().addParticle(
                Particles.FIREBALL_FLAME.get(),
                trailX, trailY, trailZ,
                particleVelX, particleVelY, particleVelZ
            );
        }

        for (int i = 0; i < 2; i++) {
            double coreX = x + (fireball.level().random.nextGaussian() * 0.05) * sizeMultiplier;
            double coreY = y + (fireball.level().random.nextGaussian() * 0.05) * sizeMultiplier;
            double coreZ = z + (fireball.level().random.nextGaussian() * 0.05) * sizeMultiplier;
            
            fireball.level().addParticle(
                Particles.FIREBALL_FLAME.get(),
                coreX, coreY, coreZ,
                (fireball.level().random.nextGaussian() * 0.01),
                (fireball.level().random.nextGaussian() * 0.01) + 0.005,
                (fireball.level().random.nextGaussian() * 0.01)
            );
        }
    }
}
