package com.leclowndu93150.particular.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface CascadeFilter {
    boolean allowsCascade(Level level, BlockPos pos);
}
