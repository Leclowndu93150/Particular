package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DripParticle.class)
public class InjectBlockLeakParticle
{
	@Inject(
			method = "getLightColor",
			at = @At("HEAD"),
			cancellable = true)
	public void getBrightness(float tint, CallbackInfoReturnable<Integer> cir)
	{
		if (!ParticularConfig.emissiveLavaDrips()) { return; }

		if (((AccessorBlockLeakParticle) this).getFluid().isSame(Fluids.LAVA))
		{
			cir.setReturnValue(15728880);
		}
	}
}