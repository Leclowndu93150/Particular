package com.leclowndu93150.particular.mixin;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChestBlockEntity.class)
public interface AccessorChestBlockEntity
{
	@Accessor("openersCounter")
	ContainerOpenersCounter getStateManager();
	@Accessor("chestLidController")
	ChestLidController getLidAnimator();
}