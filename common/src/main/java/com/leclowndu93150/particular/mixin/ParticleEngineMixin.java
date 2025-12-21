package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.particles.splashes.WaterSplashParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    @Inject(method = "createParticleGroup", at = @At("HEAD"), cancellable = true)
    private void onCreateParticleGroup(ParticleRenderType renderType, CallbackInfoReturnable<ParticleGroup<?>> cir) {
        if (renderType == WaterSplashParticle.WATER_SPLASH_TYPE) {
            cir.setReturnValue(new WaterSplashParticle.WaterSplashParticleGroup((ParticleEngine)(Object)this));
        }
    }
}
