package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderChestBlock.class)
public class InjectEnderChestBlock
{
	@Inject(
		method = "useWithoutItem",
		at = @At("HEAD"))
	private void releaseBubbles(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (!ParticularConfig.chestBubbles()) { return; }

		if (!state.getValue(BlockStateProperties.WATERLOGGED) || world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above()))
		{
			return;
		}

		Main.spawnChestBubbles(Particles.ENDER_BUBBLE.get(), world, pos);
	}
}
