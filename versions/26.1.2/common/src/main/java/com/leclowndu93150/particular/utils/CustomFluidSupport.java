package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CustomFluidSupport {
    private static Set<Fluid> waterLike = Set.of();
    private static Map<Fluid, Fluid> flowingToSource = Map.of();
    private static Set<Fluid> flowingCascades = Set.of();
    private static int lastWaterHash = -1;
    private static int lastPairHash = -1;

    private CustomFluidSupport() {}

    public static boolean isWaterLike(FluidState state) {
        refresh();
        return waterLike.contains(state.getType());
    }

    public static boolean isCascadeFlowing(FluidState state) {
        refresh();
        return flowingCascades.contains(state.getType());
    }

    public static Fluid sourceFor(FluidState flowing) {
        refresh();
        return flowingToSource.get(flowing.getType());
    }

    private static void refresh() {
        List<? extends String> waterList = ParticularConfig.COMMON.customWaterFluids.get();
        int wh = waterList.hashCode();
        if (wh != lastWaterHash) {
            Set<Fluid> set = new HashSet<>();
            for (String id : waterList) {
                Identifier rl = Identifier.tryParse(id);
                if (rl == null) continue;
                Fluid f = BuiltInRegistries.FLUID.getValue(rl);
                if (f != null && f != Fluids.EMPTY) set.add(f);
            }
            waterLike = set;
            lastWaterHash = wh;
        }

        List<? extends String> pairs = ParticularConfig.COMMON.cascadeFluidPairs.get();
        int ph = pairs.hashCode();
        if (ph != lastPairHash) {
            Map<Fluid, Fluid> map = new HashMap<>();
            for (String entry : pairs) {
                String[] parts = entry.split(",");
                if (parts.length != 2) continue;
                Identifier flowingId = Identifier.tryParse(parts[0].trim());
                Identifier sourceId = Identifier.tryParse(parts[1].trim());
                if (flowingId == null || sourceId == null) continue;
                Fluid flowing = BuiltInRegistries.FLUID.getValue(flowingId);
                Fluid source = BuiltInRegistries.FLUID.getValue(sourceId);
                if (flowing != null && flowing != Fluids.EMPTY && source != null && source != Fluids.EMPTY) map.put(flowing, source);
            }
            flowingToSource = map;
            flowingCascades = Set.copyOf(map.keySet());
            lastPairHash = ph;
        }
    }
}
