package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.CommonClass;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.utils.CustomFluidSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class InjectFlowableFluid
{
	@Inject(
		method = "animateTick",
		at = @At("TAIL"))
	protected void spawnCascades(Level p_230606_, BlockPos p_230607_, FluidState p_230608_, RandomSource p_230609_, CallbackInfo ci)
	{
		if (!ParticularConfig.cascades()) { return; }

		if (p_230608_.is(Fluids.WATER) || CustomFluidSupport.isWaterLike(p_230608_)) {
			CommonClass.updateCascade(p_230606_, p_230607_, p_230608_);
		}
	}
}
