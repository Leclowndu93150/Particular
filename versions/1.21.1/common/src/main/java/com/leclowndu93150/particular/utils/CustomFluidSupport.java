package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
            boolean allResolved = true;
            for (String id : waterList) {
                ResourceLocation rl = ResourceLocation.tryParse(id);
                if (rl == null) continue;
                Fluid f = BuiltInRegistries.FLUID.get(rl);
                if (f == null || f == Fluids.EMPTY) {
                    allResolved = false;
                    continue;
                }
                set.add(f);
            }
            waterLike = set;
            if (allResolved) lastWaterHash = wh;
        }

        List<? extends String> pairs = ParticularConfig.COMMON.cascadeFluidPairs.get();
        int ph = pairs.hashCode();
        if (ph != lastPairHash) {
            Map<Fluid, Fluid> map = new HashMap<>();
            boolean allResolved = true;
            for (String entry : pairs) {
                String[] parts = entry.split(",");
                if (parts.length != 2) continue;
                ResourceLocation flowingId = ResourceLocation.tryParse(parts[0].trim());
                ResourceLocation sourceId = ResourceLocation.tryParse(parts[1].trim());
                if (flowingId == null || sourceId == null) continue;
                Fluid flowing = BuiltInRegistries.FLUID.get(flowingId);
                Fluid source = BuiltInRegistries.FLUID.get(sourceId);
                if (flowing == null || flowing == Fluids.EMPTY || source == null || source == Fluids.EMPTY) {
                    allResolved = false;
                    continue;
                }
                map.put(flowing, source);
            }
            flowingToSource = map;
            flowingCascades = Set.copyOf(map.keySet());
            if (allResolved) lastPairHash = ph;
        }
    }
}
