package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public class InjectBarrelBlock
{
	@Inject(
		method = "useWithoutItem",
		at = @At("HEAD"))
	private void releaseBubbles(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (!ParticularConfig.barrelBubbles()) { return; }

		Direction direction = state.getValue(BlockStateProperties.FACING);

		if (direction == Direction.DOWN || !world.getFluidState(pos.offset(direction.getUnitVec3i())).is(FluidTags.WATER))
		{
			return;
		}

		for (int i = 0; i < 10; ++i)
		{
			ParticleUtils.spawnParticleOnFace(world, pos, direction, ParticleTypes.BUBBLE_COLUMN_UP, Vec3.ZERO, 0.55);
		}
	}
}