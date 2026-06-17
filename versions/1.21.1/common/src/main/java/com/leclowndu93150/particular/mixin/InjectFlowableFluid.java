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
	protected void spawnCascades(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci)
	{
		if (!ParticularConfig.cascades()) { return; }

		if (random.nextInt(10) == 0 && (state.is(Fluids.WATER) || CustomFluidSupport.isWaterLike(state))) {
			CommonClass.updateCascade(level, pos, state);
		}
	}
}
