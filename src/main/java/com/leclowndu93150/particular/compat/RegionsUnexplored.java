package com.leclowndu93150.particular.compat;

import com.leclowndu93150.particular.ClientStuff;
import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import java.awt.*;
import net.minecraft.resources.ResourceLocation;

public class RegionsUnexplored
{
	private static String MOD_ID = "regions_unexplored";

	private static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	public static void addLeaves()
	{
		// Apple oak leaves, flowering leaves, small oak leaves
		ClientStuff.registerLeafData(id("alpha_leaves"), new ClientStuff.LeafData(null)); // Classic leaves had no particles
		ClientStuff.registerLeafData(id("ashen_leaves"), new ClientStuff.LeafData(null)); // Burnt to ash
		ClientStuff.registerLeafData(id("blue_magnolia_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("pink_magnolia_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("white_magnolia_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("orange_maple_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("red_maple_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("mauve_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("silver_birch_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("enchanted_birch_leaves"), new ClientStuff.LeafData(null));
		ClientStuff.registerLeafData(id("dead_leaves"), new ClientStuff.LeafData(Particles.WHITE_OAK_LEAF.get(), new Color(0x865D40)));
		ClientStuff.registerLeafData(id("dead_pine_leaves"), new ClientStuff.LeafData(Particles.WHITE_SPRUCE_LEAF.get(), new Color(0x7D5C46)));
		ClientStuff.registerLeafData(id("blackwood_leaves"), new ClientStuff.LeafData(Particles.WHITE_SPRUCE_LEAF.get(), new Color(0x2D4519)));
		ClientStuff.registerLeafData(id("maple_leaves"), new ClientStuff.LeafData(Particles.MAPLE_LEAF.get()));
		ClientStuff.registerLeafData(id("brimwood_leaves"), new ClientStuff.LeafData(Particles.BRIMWOOD_LEAF.get(), Color.white));
		ClientStuff.registerLeafData(id("baobab_leaves"), new ClientStuff.LeafData(Particles.RU_BAOBAB_LEAF.get()));
		ClientStuff.registerLeafData(id("kapok_leaves"), new ClientStuff.LeafData(Particles.KAPOK_LEAF.get()));
		ClientStuff.registerLeafData(id("eucalyptus_leaves"), new ClientStuff.LeafData(Particles.EUCALYPTUS_LEAF.get()));
		ClientStuff.registerLeafData(id("pine_leaves"), new ClientStuff.LeafData(Particles.SPRUCE_LEAF.get()));
		ClientStuff.registerLeafData(id("redwood_leaves"), new ClientStuff.LeafData(Particles.REDWOOD_LEAF.get()));
		ClientStuff.registerLeafData(id("magnolia_leaves"), new ClientStuff.LeafData(Particles.MAGNOLIA_LEAF.get()));
		ClientStuff.registerLeafData(id("palm_leaves"), new ClientStuff.LeafData(Particles.RU_PALM_LEAF.get()));
		ClientStuff.registerLeafData(id("larch_leaves"), new ClientStuff.LeafData(Particles.LARCH_LEAF.get(), Color.white));
		ClientStuff.registerLeafData(id("golden_larch_leaves"), new ClientStuff.LeafData(Particles.GOLDEN_LARCH_LEAF.get(), Color.white));
		ClientStuff.registerLeafData(id("socotra_leaves"), new ClientStuff.LeafData(Particles.SOCOTRA_LEAF.get()));
		ClientStuff.registerLeafData(id("bamboo_leaves"), new ClientStuff.LeafData(Particles.BAMBOO_LEAF.get(), Color.white));
		ClientStuff.registerLeafData(id("willow_leaves"), new ClientStuff.LeafData(Particles.WILLOW_LEAF.get()));
		ClientStuff.registerLeafData(id("cypress_leaves"), new ClientStuff.LeafData(Particles.RU_CYPRESS_LEAF.get()));
	}
}