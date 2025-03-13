package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class InjectLevelRenderer {
	@ModifyExpressionValue(
			method = "tickRain",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/core/particles/ParticleTypes;RAIN:Lnet/minecraft/core/particles/SimpleParticleType;"
			)
	)
	private SimpleParticleType modifyParticleEffect(SimpleParticleType original, @Local FluidState fluidState) {
		if (fluidState.is(FluidTags.WATER) && ParticularConfig.rainRipples()) {
			return Particles.WATER_RIPPLE.get();
		}
		return original;
	}
}