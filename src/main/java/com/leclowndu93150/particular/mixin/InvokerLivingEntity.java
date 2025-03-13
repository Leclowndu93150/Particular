package com.leclowndu93150.particular.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = LivingEntity.class, remap = false)
public interface InvokerLivingEntity
{
	@Invoker("spawnItemParticles")
	void spawnParticles(ItemStack stack, int count);
}