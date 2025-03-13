package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public class InjectChestBlock
{
	@Inject(
		method = "use",
		at = @At("HEAD"))
	private void releaseBubbles(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (!ParticularConfig.chestBubbles()) { return; }
		if (!state.getValue(BlockStateProperties.WATERLOGGED))
		{
			return;
		}

		if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE)
		{
			Main.spawnChestBubbles(ParticleTypes.BUBBLE_COLUMN_UP, world, pos);
		}
		else
		{
			Main.spawnDoubleChestBubbles(ParticleTypes.BUBBLE_COLUMN_UP, world, pos, state);
		}
	}
}