package com.leclowndu93150.particular.mixin;

import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(value = EnderChestBlockEntity.class, remap = false)
public interface AccessorEnderChestBlockEntity
{
	@Accessor("openersCounter")
	ContainerOpenersCounter getStateManager();
	@Accessor("chestLidController")
	ChestLidController getLidAnimator();
}