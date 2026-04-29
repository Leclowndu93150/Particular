package com.leclowndu93150.particular.compat;

import com.leclowndu93150.particular.CommonClass;
import com.leclowndu93150.particular.Particles;
import net.minecraft.resources.ResourceLocation;

public class WilderWild
{
	private static String MOD_ID = "wilderwild";

	private static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void addLeaves()
	{
		CommonClass.registerLeafData(id("baobab_leaves"), new CommonClass.LeafData(Particles.WW_BAOBAB_LEAF()));
		CommonClass.registerLeafData(id("cypress_leaves"), new CommonClass.LeafData(Particles.WW_CYPRESS_LEAF()));
		CommonClass.registerLeafData(id("palm_fronds"), new CommonClass.LeafData(Particles.WW_PALM_LEAF()));
	}
}