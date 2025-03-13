package com.leclowndu93150.particular.mixin;

import net.minecraft.client.particle.DripParticle;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DripParticle.class, remap = false)
public interface AccessorBlockLeakParticle
{
	@Accessor("type")
	Fluid getFluid();
}