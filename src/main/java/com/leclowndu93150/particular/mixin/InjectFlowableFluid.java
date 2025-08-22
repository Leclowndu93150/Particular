package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FluidState;
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

		if (random.nextInt(10) == 0 && state.is(Fluids.WATER)) {
			Main.updateCascade(level, pos, state);
		}
	}
}
