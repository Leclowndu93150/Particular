package com.leclowndu93150.particular.utils;

import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    public static class Data {
        private final double[] color;

        public Data(double[] color) {
            if (color.length != 3)
                throw new IllegalArgumentException("texture color should have 3 components");
            this.color = Arrays.copyOf(color, color.length);
        }

        public double[] getColor() {
            return Arrays.copyOf(color, color.length);
        }
    }

    public static final Map<ResourceLocation, Data> INST = new HashMap<>();

    public static void clear() {
        INST.clear();
    }
}