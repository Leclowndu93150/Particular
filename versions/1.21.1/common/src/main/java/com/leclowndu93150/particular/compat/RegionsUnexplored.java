package com.leclowndu93150.particular.compat;

import com.leclowndu93150.particular.CommonClass;
import com.leclowndu93150.particular.Particles;
import java.awt.*;
import net.minecraft.resources.ResourceLocation;

public class RegionsUnexplored
{
	private static String MOD_ID = "regions_unexplored";

	private static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void addLeaves()
	{
		// Apple oak leaves, flowering leaves, small oak leaves
		CommonClass.registerLeafData(id("alpha_leaves"), new CommonClass.LeafData(null)); // Classic leaves had no particles
		CommonClass.registerLeafData(id("ashen_leaves"), new CommonClass.LeafData(null)); // Burnt to ash
		CommonClass.registerLeafData(id("blue_magnolia_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("pink_magnolia_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("white_magnolia_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("orange_maple_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("red_maple_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("mauve_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("silver_birch_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("enchanted_birch_leaves"), new CommonClass.LeafData(null));
		CommonClass.registerLeafData(id("dead_leaves"), new CommonClass.LeafData(Particles.WHITE_OAK_LEAF(), new Color(0x865D40)));
		CommonClass.registerLeafData(id("dead_pine_leaves"), new CommonClass.LeafData(Particles.WHITE_SPRUCE_LEAF(), new Color(0x7D5C46)));
		CommonClass.registerLeafData(id("blackwood_leaves"), new CommonClass.LeafData(Particles.WHITE_SPRUCE_LEAF(), new Color(0x2D4519)));
		CommonClass.registerLeafData(id("maple_leaves"), new CommonClass.LeafData(Particles.MAPLE_LEAF()));
		CommonClass.registerLeafData(id("brimwood_leaves"), new CommonClass.LeafData(Particles.BRIMWOOD_LEAF(), Color.white));
		CommonClass.registerLeafData(id("baobab_leaves"), new CommonClass.LeafData(Particles.RU_BAOBAB_LEAF()));
		CommonClass.registerLeafData(id("kapok_leaves"), new CommonClass.LeafData(Particles.KAPOK_LEAF()));
		CommonClass.registerLeafData(id("eucalyptus_leaves"), new CommonClass.LeafData(Particles.EUCALYPTUS_LEAF()));
		CommonClass.registerLeafData(id("pine_leaves"), new CommonClass.LeafData(Particles.SPRUCE_LEAF()));
		CommonClass.registerLeafData(id("redwood_leaves"), new CommonClass.LeafData(Particles.REDWOOD_LEAF()));
		CommonClass.registerLeafData(id("magnolia_leaves"), new CommonClass.LeafData(Particles.MAGNOLIA_LEAF()));
		CommonClass.registerLeafData(id("palm_leaves"), new CommonClass.LeafData(Particles.RU_PALM_LEAF()));
		CommonClass.registerLeafData(id("larch_leaves"), new CommonClass.LeafData(Particles.LARCH_LEAF(), Color.white));
		CommonClass.registerLeafData(id("golden_larch_leaves"), new CommonClass.LeafData(Particles.GOLDEN_LARCH_LEAF(), Color.white));
		CommonClass.registerLeafData(id("socotra_leaves"), new CommonClass.LeafData(Particles.SOCOTRA_LEAF()));
		CommonClass.registerLeafData(id("bamboo_leaves"), new CommonClass.LeafData(Particles.BAMBOO_LEAF(), Color.white));
		CommonClass.registerLeafData(id("willow_leaves"), new CommonClass.LeafData(Particles.WILLOW_LEAF()));
		CommonClass.registerLeafData(id("cypress_leaves"), new CommonClass.LeafData(Particles.RU_CYPRESS_LEAF()));
	}
}