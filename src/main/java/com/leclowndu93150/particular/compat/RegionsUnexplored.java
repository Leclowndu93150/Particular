package com.leclowndu93150.particular.compat;

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
		Main.registerLeafData(id("alpha_leaves"), new Main.LeafData(null)); // Classic leaves had no particles
		Main.registerLeafData(id("ashen_leaves"), new Main.LeafData(null)); // Burnt to ash
		Main.registerLeafData(id("blue_magnolia_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("pink_magnolia_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("white_magnolia_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("orange_maple_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("red_maple_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("mauve_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("silver_birch_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("enchanted_birch_leaves"), new Main.LeafData(null));
		Main.registerLeafData(id("dead_leaves"), new Main.LeafData(Particles.WHITE_OAK_LEAF.get(), new Color(0x865D40)));
		Main.registerLeafData(id("dead_pine_leaves"), new Main.LeafData(Particles.WHITE_SPRUCE_LEAF.get(), new Color(0x7D5C46)));
		Main.registerLeafData(id("blackwood_leaves"), new Main.LeafData(Particles.WHITE_SPRUCE_LEAF.get(), new Color(0x2D4519)));
		Main.registerLeafData(id("maple_leaves"), new Main.LeafData(Particles.MAPLE_LEAF.get()));
		Main.registerLeafData(id("brimwood_leaves"), new Main.LeafData(Particles.BRIMWOOD_LEAF.get(), Color.white));
		Main.registerLeafData(id("baobab_leaves"), new Main.LeafData(Particles.RU_BAOBAB_LEAF.get()));
		Main.registerLeafData(id("kapok_leaves"), new Main.LeafData(Particles.KAPOK_LEAF.get()));
		Main.registerLeafData(id("eucalyptus_leaves"), new Main.LeafData(Particles.EUCALYPTUS_LEAF.get()));
		Main.registerLeafData(id("pine_leaves"), new Main.LeafData(Particles.SPRUCE_LEAF.get()));
		Main.registerLeafData(id("redwood_leaves"), new Main.LeafData(Particles.REDWOOD_LEAF.get()));
		Main.registerLeafData(id("magnolia_leaves"), new Main.LeafData(Particles.MAGNOLIA_LEAF.get()));
		Main.registerLeafData(id("palm_leaves"), new Main.LeafData(Particles.RU_PALM_LEAF.get()));
		Main.registerLeafData(id("larch_leaves"), new Main.LeafData(Particles.LARCH_LEAF.get(), Color.white));
		Main.registerLeafData(id("golden_larch_leaves"), new Main.LeafData(Particles.GOLDEN_LARCH_LEAF.get(), Color.white));
		Main.registerLeafData(id("socotra_leaves"), new Main.LeafData(Particles.SOCOTRA_LEAF.get()));
		Main.registerLeafData(id("bamboo_leaves"), new Main.LeafData(Particles.BAMBOO_LEAF.get(), Color.white));
		Main.registerLeafData(id("willow_leaves"), new Main.LeafData(Particles.WILLOW_LEAF.get()));
		Main.registerLeafData(id("cypress_leaves"), new Main.LeafData(Particles.RU_CYPRESS_LEAF.get()));
	}
}