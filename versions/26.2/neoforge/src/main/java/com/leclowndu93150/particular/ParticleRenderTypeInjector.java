package com.leclowndu93150.particular;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;

public class ParticleRenderTypeInjector {
    public static void addRenderType(ParticleRenderType renderType) {
        try {
            Field field = ParticleEngine.class.getDeclaredField("RENDER_ORDER");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            List<ParticleRenderType> mutable = new ArrayList<>((List<ParticleRenderType>) field.get(null));

            mutable.add(renderType);

            field.set(null, mutable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to modify ParticleEngine.RENDER_ORDER", e);
        }
    }
}
