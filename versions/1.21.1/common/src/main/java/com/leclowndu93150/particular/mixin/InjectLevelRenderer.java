package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.utils.CustomFluidSupport;
import com.leclowndu93150.particular.utils.Rainbow;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class InjectLevelRenderer {
	@Inject(
			method = "renderSky",
			at = @At("TAIL")
	)
	private void renderRainbow(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
		Rainbow.render(frustumMatrix, partialTick, camera, isFoggy);
	}

	@ModifyExpressionValue(
			method = "tickRain",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/core/particles/ParticleTypes;RAIN:Lnet/minecraft/core/particles/SimpleParticleType;"
			)
	)
	private SimpleParticleType modifyParticleEffect(SimpleParticleType original, @Local FluidState fluidState) {
		if ((fluidState.is(FluidTags.WATER) || CustomFluidSupport.isWaterLike(fluidState)) && ParticularConfig.rainRipples()) {
			return Particles.WATER_RIPPLE();
		}
		return original;
	}
}