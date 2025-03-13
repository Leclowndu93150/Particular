package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowingFluid.class)
public class InjectFlowableFluid
{
	@Inject(
		method = "tick",
		at = @At("TAIL"))
	protected void spawnCascades(Level level, BlockPos pos, FluidState state, CallbackInfo ci)
	{
		if (!ParticularConfig.cascades()) { return; }

		Main.updateCascade(level, pos, state);
	}
}
