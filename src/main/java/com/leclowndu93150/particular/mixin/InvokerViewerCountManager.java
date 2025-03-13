package com.leclowndu93150.particular.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ContainerOpenersCounter.class, remap = false)
public interface InvokerViewerCountManager {
    @Invoker("onOpen")
    void invokeOnContainerOpen(Level level, BlockPos pos, BlockState state);

    @Invoker("onClose")
    void invokeOnContainerClose(Level level, BlockPos pos, BlockState state);

    @Invoker("scheduleRecheck")
    static void invokeScheduleBlockTick(Level level, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }
}