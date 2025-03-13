package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class InjectChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity
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

	public InjectChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	@Unique
	private static boolean getSoulSand(Level world, BlockPos pos, BlockState state)
	{
		pos = pos.below();

		if (world.getBlockState(pos).getBlock() == Blocks.SOUL_SAND) { return true; }

		if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.RIGHT)
		{
			BlockPos pos2 = pos.offset(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(Direction.Axis.Y).getNormal());

			return world.getBlockState(pos2).getBlock() == Blocks.SOUL_SAND;
		}

		return false;
	}

	@Inject(
			method = "lidAnimateTick",
			at = @At("TAIL"))
	private static void randomlyOpen(Level world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
		if (!ParticularConfig.soulSandBubbles()) { return; }

		if (!state.getValue(BlockStateProperties.WATERLOGGED) ||
				state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.LEFT ||
				!getSoulSand(world, pos, state))
		{
			return;
		}

		InjectChestBlockEntity injected = (InjectChestBlockEntity)(Object)blockEntity;

		if (--injected.ticksUntilNextSwitch <= 0) {
			ContainerOpenersCounter manager = ((AccessorChestBlockEntity) blockEntity).getStateManager();
			if (injected.isOpen) {
				injected.isOpen = false;
				injected.ticksUntilNextSwitch = world.random.nextIntBetweenInclusive(minClosedTime, maxClosedTime);
				((AccessorChestBlockEntity) blockEntity).getLidAnimator().shouldBeOpen(false);
				((InvokerViewerCountManager)manager).invokeOnContainerClose(world, pos, blockEntity.getBlockState());
			} else {
				injected.isOpen = true;
				injected.ticksUntilNextSwitch = world.random.nextIntBetweenInclusive(minOpenTime, maxOpenTime);
				((AccessorChestBlockEntity) blockEntity).getLidAnimator().shouldBeOpen(true);
				((InvokerViewerCountManager)manager).invokeOnContainerOpen(world, pos, blockEntity.getBlockState());
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.AMBIENT, 1f, 1f, true);
			}
		}

		if (injected.isOpen &&
				injected.ticksUntilNextSwitch > 10 &&
				injected.ticksUntilNextSwitch % 2 == 0)
		{
			if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE) {
				Main.spawnBubble(ParticleTypes.BUBBLE_COLUMN_UP, world, pos);
			} else {
				Main.spawnDoubleBubbles(ParticleTypes.BUBBLE_COLUMN_UP, world, pos, state);
			}
		}
	}
}