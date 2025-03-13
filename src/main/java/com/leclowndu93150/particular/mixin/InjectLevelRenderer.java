package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class InjectLevelRenderer
{
	@Inject(
			method = "tickRain",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CampfireBlock;isLitCampfire(Lnet/minecraft/world/level/block/state/BlockState;)Z"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V")
			),
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;minecraft:Lnet/minecraft/client/Minecraft;",
					opcode = Opcodes.GETFIELD))
	private void modifyParticleEffect(Camera camera, CallbackInfo ci, @Local FluidState fluidState, @Local LocalRef<ParticleOptions> particleEffect)
	{
		if (fluidState.is(FluidTags.WATER) && ParticularConfig.rainRipples())
		{
			particleEffect.set(Particles.WATER_RIPPLE.get());
		}
	}
}