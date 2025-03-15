package com.leclowndu93150.particular.compat;

import com.leclowndu93150.particular.ClientStuff;
import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import java.awt.*;
import net.minecraft.resources.ResourceLocation;

public class Traverse
{
	private static String MOD_ID = "traverse";

	private static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	public static void addLeaves()
	{
		ClientStuff.registerLeafData(id("brown_autumnal_leaves"), new ClientStuff.LeafData(Particles.WHITE_OAK_LEAF.get(), new Color(0x734B27)));
		ClientStuff.registerLeafData(id("red_autumnal_leaves"), new ClientStuff.LeafData(Particles.WHITE_OAK_LEAF.get(), new Color(0xB64430)));
		ClientStuff.registerLeafData(id("orange_autumnal_leaves"), new ClientStuff.LeafData(Particles.WHITE_OAK_LEAF.get(), new Color(0xEF8F1D)));
		ClientStuff.registerLeafData(id("yellow_autumnal_leaves"), new ClientStuff.LeafData(Particles.WHITE_OAK_LEAF.get(), new Color(0xE9D131)));
		ClientStuff.registerLeafData(id("fir_leaves"), new ClientStuff.LeafData(Particles.WHITE_SPRUCE_LEAF.get(), new Color(0x1B4719)));
	}
}