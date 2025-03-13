package com.leclowndu93150.particular.compat;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import net.minecraft.resources.ResourceLocation;

public class WilderWild
{
	private static String MOD_ID = "wilderwild";

	private static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	public static void addLeaves()
	{
		Main.registerLeafData(id("baobab_leaves"), new Main.LeafData(Particles.WW_BAOBAB_LEAF.get()));
		Main.registerLeafData(id("cypress_leaves"), new Main.LeafData(Particles.WW_CYPRESS_LEAF.get()));
		Main.registerLeafData(id("palm_fronds"), new Main.LeafData(Particles.WW_PALM_LEAF.get()));
	}
}