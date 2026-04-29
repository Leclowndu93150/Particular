package com.leclowndu93150.particular.compat;

import com.leclowndu93150.particular.CommonClass;
import com.leclowndu93150.particular.Particles;
import java.awt.*;
import net.minecraft.resources.ResourceLocation;

public class Traverse
{
	private static String MOD_ID = "traverse";

	private static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void addLeaves()
	{
		CommonClass.registerLeafData(id("brown_autumnal_leaves"), new CommonClass.LeafData(Particles.WHITE_OAK_LEAF(), new Color(0x734B27)));
		CommonClass.registerLeafData(id("red_autumnal_leaves"), new CommonClass.LeafData(Particles.WHITE_OAK_LEAF(), new Color(0xB64430)));
		CommonClass.registerLeafData(id("orange_autumnal_leaves"), new CommonClass.LeafData(Particles.WHITE_OAK_LEAF(), new Color(0xEF8F1D)));
		CommonClass.registerLeafData(id("yellow_autumnal_leaves"), new CommonClass.LeafData(Particles.WHITE_OAK_LEAF(), new Color(0xE9D131)));
		CommonClass.registerLeafData(id("fir_leaves"), new CommonClass.LeafData(Particles.WHITE_SPRUCE_LEAF(), new Color(0x1B4719)));
	}
}