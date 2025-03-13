package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
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
		method = "use",
		at = @At("HEAD"))
	private void releaseBubbles(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (!ParticularConfig.chestBubbles()) { return; }

		if (!state.getValue(BlockStateProperties.WATERLOGGED) || level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above()))
		{
			return;
		}

		Main.spawnChestBubbles(Particles.ENDER_BUBBLE.get(), level, pos);
	}
}
