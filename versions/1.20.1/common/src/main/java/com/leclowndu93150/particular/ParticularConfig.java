package com.leclowndu93150.particular;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class ParticularConfig {
	public static final CommonConfig COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static {
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = specPair.getLeft();
		COMMON_SPEC = specPair.getRight();
	}

	public static class CommonConfig {
		public final ForgeConfigSpec.BooleanValue waterSplash;
		public final ForgeConfigSpec.BooleanValue cascades;
		public final ForgeConfigSpec.BooleanValue waterfallSpray;
		public final ForgeConfigSpec.BooleanValue fireflies;
		public final ForgeConfigSpec.BooleanValue fallingLeaves;
		public final ForgeConfigSpec.BooleanValue caveDust;
		public final ForgeConfigSpec.BooleanValue chestBubbles;
		public final ForgeConfigSpec.BooleanValue soulSandBubbles;
		public final ForgeConfigSpec.BooleanValue barrelBubbles;
		public final ForgeConfigSpec.BooleanValue poppingBubbles;
		public final ForgeConfigSpec.BooleanValue rainRipples;
		public final ForgeConfigSpec.BooleanValue waterDripRipples;
		public final ForgeConfigSpec.BooleanValue cakeEatingParticles;
		public final ForgeConfigSpec.BooleanValue emissiveLavaDrips;
		public final ForgeConfigSpec.BooleanValue lavaSplash;

		public final ForgeConfigSpec.IntValue fireflyStartTime;
		public final ForgeConfigSpec.IntValue fireflyEndTime;
		public final ForgeConfigSpec.DoubleValue fireflyMinTemp;
		public final ForgeConfigSpec.DoubleValue fireflyMaxTemp;
		public final ForgeConfigSpec.BooleanValue fireflyCanSpawnInRain;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> fireflyDailyRandom;
		public final ForgeConfigSpec.DoubleValue fireflyGrassFrequency;
		public final ForgeConfigSpec.DoubleValue fireflyTallGrassFrequency;
		public final ForgeConfigSpec.DoubleValue fireflyFlowersFrequency;
		public final ForgeConfigSpec.DoubleValue fireflyTallFlowersFrequency;
		public final ForgeConfigSpec.DoubleValue fireflyCustomBlockFrequency;

		public final ForgeConfigSpec.IntValue fallingLeavesSpawnChance;
		public final ForgeConfigSpec.BooleanValue fallingLeavesSpawnRipples;
		public final ForgeConfigSpec.BooleanValue fallingLeavesLayFlatOnGround;
		public final ForgeConfigSpec.BooleanValue fallingLeavesLayFlatRightAngles;

		public final ForgeConfigSpec.DoubleValue waterSplashMinFallDistance;
		public final ForgeConfigSpec.BooleanValue waterSplashSmallDroplets;
		public final ForgeConfigSpec.DoubleValue waterSplashOpacity;
		public final ForgeConfigSpec.BooleanValue waterSplashSoftEntryParticles;
		public final ForgeConfigSpec.BooleanValue cuboidWaterfallSpray;
		public final ForgeConfigSpec.BooleanValue cuboidSplashDroplets;
		public final ForgeConfigSpec.BooleanValue waterCuboidBiomeTint;
		public final ForgeConfigSpec.IntValue waterCuboidColor;
		public final ForgeConfigSpec.IntValue lavaCuboidColor;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> customWaterFluids;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> cascadeFluidPairs;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireflyBiomes;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireflySpawnBlocks;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireflyBiomeColors;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireflyColorPool;

		public final ForgeConfigSpec.IntValue caveDustSpawnChance;
		public final ForgeConfigSpec.IntValue caveDustBaseMaxAge;
		public final ForgeConfigSpec.IntValue caveDustColor;
		public final ForgeConfigSpec.IntValue caveDustFadeDuration;
		public final ForgeConfigSpec.DoubleValue caveDustMaxAcceleration;
		public final ForgeConfigSpec.IntValue caveDustAccelChangeChance;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> caveDustExcludeBiomes;

		CommonConfig(ForgeConfigSpec.Builder builder) {
			builder.comment("Particular Mod Configuration").push("general");

			builder.comment("Enable/Disable Effects").push("enabledEffects");
			waterSplash = builder.comment("Enable water splash particles").define("waterSplash", true);
			cascades = builder.comment("Enable cascade particles").define("cascades", true);
			waterfallSpray = builder.comment("Enable waterfall spray particles").define("waterfallSpray", true);
			fireflies = builder.comment("Enable firefly particles").define("fireflies", true);
			fallingLeaves = builder.comment("Enable falling leaves particles").define("fallingLeaves", true);
			caveDust = builder.comment("Enable cave dust particles").define("caveDust", true);
			chestBubbles = builder.comment("Enable chest bubbles").define("chestBubbles", true);
			soulSandBubbles = builder.comment("Enable soul sand bubbles").define("soulSandBubbles", true);
			barrelBubbles = builder.comment("Enable barrel bubbles").define("barrelBubbles", true);
			poppingBubbles = builder.comment("Enable popping bubbles").define("poppingBubbles", true);
			rainRipples = builder.comment("Enable rain ripples").define("rainRipples", true);
			waterDripRipples = builder.comment("Enable water drip ripples").define("waterDripRipples", true);
			cakeEatingParticles = builder.comment("Enable cake eating particles").define("cakeEatingParticles", true);
			emissiveLavaDrips = builder.comment("Enable emissive lava drips").define("emissiveLavaDrips", true);
			lavaSplash = builder.comment("Enable lava splash particles when entities hit lava").define("lavaSplash", true);
			builder.pop();

			builder.comment("Advanced Particle Settings").push("advancedSettings");

			builder.push("fireflySettings");
			fireflyStartTime = builder.comment("Time when fireflies start spawning").defineInRange("startTime", 12000, 0, 23999);
			fireflyEndTime = builder.comment("Time when fireflies stop spawning").defineInRange("endTime", 23000, 0, 23999);
			fireflyMinTemp = builder.comment("Minimum temperature for fireflies to spawn").defineInRange("minTemp", 0.5f, 0.0, 2.0);
			fireflyMaxTemp = builder.comment("Maximum temperature for fireflies to spawn").defineInRange("maxTemp", 0.99f, 0.0, 2.0);
			fireflyCanSpawnInRain = builder.comment("Whether fireflies can spawn in rain").define("canSpawnInRain", false);

			builder.push("frequencyModifiers");
			fireflyDailyRandom = builder.comment("Daily random factors for firefly spawning")
					.defineList("dailyRandom", Arrays.asList(0.0, 0.0, 0.0, 0.33, 0.66, 1.0), entry -> entry instanceof Double);
			fireflyGrassFrequency = builder.comment("Frequency modifier for grass").defineInRange("grass", 1.0/6.0, 0.0, 1.0);
			fireflyTallGrassFrequency = builder.comment("Frequency modifier for tall grass").defineInRange("tallGrass", 1.0/12.0, 0.0, 1.0);
			fireflyFlowersFrequency = builder.comment("Frequency modifier for flowers").defineInRange("flowers", 1.0, 0.0, 1.0);
			fireflyTallFlowersFrequency = builder.comment("Frequency modifier for tall flowers").defineInRange("tallFlowers", 0.5, 0.0, 1.0);
			fireflyCustomBlockFrequency = builder.comment("Frequency modifier for blocks listed in fireflySpawnBlocks").defineInRange("customBlocks", 1.0/6.0, 0.0, 1.0);
			builder.pop();

			fireflyBiomes = builder.comment("Biomes where fireflies always spawn (bypasses temperature check). Example: \"minecraft:swamp\"")
					.defineList("biomes", List.<String>of(), entry -> entry instanceof String);
			fireflySpawnBlocks = builder.comment("Extra blocks that spawn fireflies. Example: \"tfc:plant/cattail\"")
					.defineList("spawnBlocks", List.<String>of(), entry -> entry instanceof String);
			fireflyBiomeColors = builder.comment("Per-biome firefly RGB hex tints. Format: \"biomeId|RRGGBB\". Example: \"minecraft:swamp|FFD27F\". Takes priority over fireflyColorPool.")
					.defineList("biomeColors", List.<String>of(), entry -> entry instanceof String);
			fireflyColorPool = builder.comment("Random RGB hex colors picked per firefly when no biomeColors entry matches. Empty = use built-in yellow/blue/red mix. Format: \"RRGGBB\".")
					.defineList("colorPool", List.<String>of(), entry -> entry instanceof String);
			builder.pop();

			builder.push("customFluidsSettings");
			customWaterFluids = builder.comment("Fluid IDs treated like vanilla water for splash, rain ripples, and waterfall spray. Example: \"tfc:river_water\"")
					.defineList("waterLikeFluids", Arrays.asList(
									"tfc:river_water",
									"tfc:flowing_salt_water",
									"tfc:salt_water",
									"tfc:flowing_spring_water",
									"tfc:spring_water"),
							entry -> entry instanceof String);
			cascadeFluidPairs = builder.comment("Flowing,source fluid pairs that produce cascade waterfalls. Example: \"mymod:flowing_water,mymod:water\". TFC fluids do not produce cascades because they have no flowing variant.")
					.defineList("cascadeFluidPairs", List.<String>of(), entry -> entry instanceof String);
			builder.pop();

			builder.push("fallingLeavesSettings");
			fallingLeavesSpawnChance = builder.comment("Chance of spawning falling leaves (higher = less frequent)")
					.defineInRange("spawnChance", 60, 1, Integer.MAX_VALUE);
			fallingLeavesSpawnRipples = builder.comment("Whether falling leaves create ripples when landing on water")
					.define("spawnRipples", true);
			fallingLeavesLayFlatOnGround = builder.comment("Whether falling leaves lay flat on the ground")
					.define("layFlatOnGround", true);
			fallingLeavesLayFlatRightAngles = builder.comment("Whether falling leaves lay at right angles")
					.define("layFlatRightAngles", false);
			builder.pop();

			builder.push("waterSplashSettings");
			waterSplashMinFallDistance = builder.comment("Minimum fall distance (in blocks) required to trigger water splash particles")
					.defineInRange("minFallDistance", 0.0, 0.0, 100.0);
			waterSplashSmallDroplets = builder.comment("Whether small splashes should emit water droplet particles")
					.define("smallDroplets", false);
			waterSplashOpacity = builder.comment("Opacity of water and lava splash particles")
					.defineInRange("splashOpacity", 0.75, 0.0, 1.0);
			waterSplashSoftEntryParticles = builder.comment("Whether soft water entries should emit bubbles and tiny falling-water droplets instead of a full splash")
					.define("softEntryParticles", true);
			cuboidWaterfallSpray = builder.comment("Use 1-voxel cuboid particles for waterfall spray")
					.define("cuboidWaterfallSpray", false);
			cuboidSplashDroplets = builder.comment("Use 1-voxel cuboid particles for splash droplets")
					.define("cuboidSplashDroplets", true);
			waterCuboidBiomeTint = builder.comment("Tint cuboid waterfall spray using the local biome water color")
					.define("waterCuboidBiomeTint", true);
			waterCuboidColor = builder.comment("Fallback color of cuboid waterfall spray when biome tinting is disabled (RGB hex)")
					.defineInRange("waterCuboidColor", 0xFFFFFF, 0, 0xFFFFFF);
			lavaCuboidColor = builder.comment("Color of cuboid lava droplets (RGB hex)")
					.defineInRange("lavaCuboidColor", 0xFF681F, 0, 0xFFFFFF);
			builder.pop();

			builder.push("caveDustSettings");
			caveDustSpawnChance = builder.comment("Chance of spawning cave dust (higher = less frequent)")
					.defineInRange("spawnChance", 700, 1, Integer.MAX_VALUE);
			caveDustBaseMaxAge = builder.comment("Base maximum age of cave dust particles")
					.defineInRange("baseMaxAge", 200, 1, Integer.MAX_VALUE);
			caveDustColor = builder.comment("OwOColor of cave dust particles (RGB hex)")
					.defineInRange("color", 0x808080, 0, 0xFFFFFF);
			caveDustFadeDuration = builder.comment("Duration of fade effect for cave dust particles")
					.defineInRange("fadeDuration", 20, 0, Integer.MAX_VALUE);
			caveDustMaxAcceleration = builder.comment("Maximum acceleration of cave dust particles")
					.defineInRange("maxAcceleration", 0.03f, 0.0, 1.0);
			caveDustAccelChangeChance = builder.comment("Chance of changing acceleration for cave dust particles")
					.defineInRange("accelChangeChance", 180, 1, Integer.MAX_VALUE);
			caveDustExcludeBiomes = builder.comment("Biomes where cave dust won't spawn")
					.defineList("excludeBiomes", Arrays.asList(
									"minecraft:lush_caves",
									"minecraft:dripstone_caves",
									"minecraft:deep_dark"),
							entry -> entry instanceof String);
			builder.pop();

			builder.pop();
			builder.pop();
		}
	}

	public static boolean waterSplash() { return COMMON.waterSplash.get(); }
	public static boolean cascades() { return COMMON.cascades.get(); }
	public static boolean waterfallSpray() { return COMMON.waterfallSpray.get(); }
	public static boolean fireflies() { return COMMON.fireflies.get(); }
	public static boolean fallingLeaves() { return COMMON.fallingLeaves.get(); }
	public static boolean caveDust() { return COMMON.caveDust.get(); }
	public static boolean chestBubbles() { return COMMON.chestBubbles.get(); }
	public static boolean soulSandBubbles() { return COMMON.soulSandBubbles.get(); }
	public static boolean barrelBubbles() { return COMMON.barrelBubbles.get(); }
	public static boolean poppingBubbles() { return COMMON.poppingBubbles.get(); }
	public static boolean rainRipples() { return COMMON.rainRipples.get(); }
	public static boolean waterDripRipples() { return COMMON.waterDripRipples.get(); }
	public static boolean cakeEatingParticles() { return COMMON.cakeEatingParticles.get(); }
	public static boolean emissiveLavaDrips() { return COMMON.emissiveLavaDrips.get(); }
	public static boolean lavaSplash() { return COMMON.lavaSplash.get(); }

	public static List<ResourceLocation> getCaveDustExcludeBiomes() {
		return COMMON.caveDustExcludeBiomes.get().stream()
				.map(ResourceLocation::tryParse)
				.toList();
	}
}
