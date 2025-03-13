package com.leclowndu93150.particular.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Particle.class)
public interface AccessorParticle
{
	@Accessor("stoppedByCollision")
	void setStopped(boolean val);
}
