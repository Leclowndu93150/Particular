package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderChestBlockEntity.class)
public abstract class InjectEnderChestBlockEntity extends BlockEntity implements LidBlockEntity
{
	@Unique
	private int ticksUntilNextSwitch = 20;
	@Unique
	private boolean isOpen = false;

	@Unique
	private static final int minClosedTime = 20 * 8;
	@Unique
	private static final int maxClosedTime = 20 * 24;
	@Unique
	private static final int minOpenTime = 20 * 2;
	@Unique
	private static final int maxOpenTime = 20 * 3;

	public InjectEnderChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@Inject(
		method = "lidAnimateTick",
		at = @At("TAIL"))
	private static void randomlyOpen(Level world, BlockPos pos, BlockState state, InjectEnderChestBlockEntity blockEntity, CallbackInfo ci)
	{
		if (!ParticularConfig.soulSandBubbles()) { return; }

		if (!state.getValue(BlockStateProperties.WATERLOGGED) ||
			world.getBlockState(pos.below()).getBlock() != Blocks.SOUL_SAND ||
			world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above()))
		{
			return;
		}

		if (--blockEntity.ticksUntilNextSwitch <= 0)
		{
			ContainerOpenersCounter manager = ((AccessorEnderChestBlockEntity) blockEntity).getStateManager();
			if (blockEntity.isOpen)
			{
				blockEntity.isOpen = false;
				blockEntity.ticksUntilNextSwitch = world.random.nextIntBetweenInclusive(minClosedTime, maxClosedTime);
				((AccessorEnderChestBlockEntity) blockEntity).getLidAnimator().shouldBeOpen(false);
				((InvokerViewerCountManager)manager).invokeOnContainerClose(world, pos, blockEntity.getBlockState());
			}
			else
			{
				blockEntity.isOpen = true;
				blockEntity.ticksUntilNextSwitch = world.random.nextIntBetweenInclusive(minOpenTime, maxOpenTime);
				((AccessorEnderChestBlockEntity) blockEntity).getLidAnimator().shouldBeOpen(true);
				((InvokerViewerCountManager)manager).invokeOnContainerOpen(world, pos, blockEntity.getBlockState());
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.AMBIENT, 1f, 1f, true);
			}
		}

		if (blockEntity.isOpen &&
			blockEntity.ticksUntilNextSwitch > 10 &&
			blockEntity.ticksUntilNextSwitch % 2 == 0)
		{
			Main.spawnBubble(Particles.ENDER_BUBBLE.get(), world, pos);
		}
	}
}