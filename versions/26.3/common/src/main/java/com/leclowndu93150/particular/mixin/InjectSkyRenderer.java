package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.utils.Rainbow;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class InjectSkyRenderer
{
	@Inject(
		method = "render",
		at = @At("HEAD"))
	private void prepareRainbow(CallbackInfo ci)
	{
		Rainbow.prepare();
	}

	@Inject(
		method = "renderSunMoonAndStars",
		at = @At("TAIL"))
	private void renderRainbow(CallbackInfo ci, @Local(argsOnly = true) RenderPass renderPass)
	{
		Rainbow.draw(renderPass);
	}
}
