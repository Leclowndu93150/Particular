package com.leclowndu93150.particular.mixin;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Biome.class)
public interface AccessorBiome
{
	@Accessor("climateSettings")
	Biome.ClimateSettings getWeather();
}