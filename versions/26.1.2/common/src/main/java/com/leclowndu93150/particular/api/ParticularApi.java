package com.leclowndu93150.particular.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ParticularApi {
    private static final List<CascadeFilter> CASCADE_FILTERS = new CopyOnWriteArrayList<>();

    private ParticularApi() {
    }

    public static void addCascadeFilter(CascadeFilter filter) {
        CASCADE_FILTERS.add(filter);
    }

    public static boolean allowsCascade(Level level, BlockPos pos) {
        for (CascadeFilter filter : CASCADE_FILTERS) {
            if (!filter.allowsCascade(level, pos)) {
                return false;
            }
        }
        return true;
    }
}
