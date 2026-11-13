package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.utils.Rainbow;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class InjectSkyRenderer
{
	@Shadow
	@Final
	private RenderTarget renderTarget;

	@Inject(
		method = "renderSunMoonAndStars",
		at = @At("TAIL"))
	private void renderRainbow(CallbackInfo ci)
	{
		Rainbow.render(renderTarget);
	}
}
