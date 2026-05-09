package com.leclowndu93150.particular.mixin;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixinNeo {
    @Shadow
    @Final
    @Mutable
    public static List<ParticleRenderType> RENDER_ORDER;

    static {
        RENDER_ORDER = new ArrayList<>(RENDER_ORDER);
    }
}
