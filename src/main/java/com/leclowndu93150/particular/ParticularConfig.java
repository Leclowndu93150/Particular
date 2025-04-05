package com.leclowndu93150.particular;

import com.leclowndu93150.particular.utils.IrisCompat;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class ParticularConfig {
	public static final CommonConfig COMMON;
	public static final ModConfigSpec COMMON_SPEC;

	static {
		final Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = specPair.getLeft();
		COMMON_SPEC = specPair.getRight();
	}

	public static void register(ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	public static class CommonConfig {
		// Enabled Effects
		public final ModConfigSpec.BooleanValue waterSplash;
		public final ModConfigSpec.BooleanValue cascades;
		public final ModConfigSpec.BooleanValue waterfallSpray;
		public final ModConfigSpec.BooleanValue fireflies;
		public final ModConfigSpec.BooleanValue fallingLeaves;
		public final ModConfigSpec.BooleanValue caveDust;
		public final ModConfigSpec.BooleanValue chestBubbles;
		public final ModConfigSpec.BooleanValue soulSandBubbles;
		public final ModConfigSpec.BooleanValue barrelBubbles;
		public final ModConfigSpec.BooleanValue poppingBubbles;
		public final ModConfigSpec.BooleanValue rainRipples;
		public final ModConfigSpec.BooleanValue waterDripRipples;
		public final ModConfigSpec.BooleanValue cakeEatingParticles;
		public final ModConfigSpec.BooleanValue emissiveLavaDrips;

		// Firefly Settings
		public final ModConfigSpec.IntValue fireflyStartTime;
		public final ModConfigSpec.IntValue fireflyEndTime;
		public final ModConfigSpec.DoubleValue fireflyMinTemp;
		public final ModConfigSpec.DoubleValue fireflyMaxTemp;
		public final ModConfigSpec.BooleanValue fireflyCanSpawnInRain;
		public final ModConfigSpec.ConfigValue<List<? extends Double>> fireflyDailyRandom;
		public final ModConfigSpec.DoubleValue fireflyGrassFrequency;
		public final ModConfigSpec.DoubleValue fireflyTallGrassFrequency;
		public final ModConfigSpec.DoubleValue fireflyFlowersFrequency;
		public final ModConfigSpec.DoubleValue fireflyTallFlowersFrequency;

		// Falling Leaves Settings
		public final ModConfigSpec.IntValue fallingLeavesSpawnChance;
		public final ModConfigSpec.BooleanValue fallingLeavesSpawnRipples;
		public final ModConfigSpec.BooleanValue fallingLeavesLayFlatOnGround;
		public final ModConfigSpec.BooleanValue fallingLeavesLayFlatRightAngles;

		// Cave Dust Settings
		public final ModConfigSpec.IntValue caveDustSpawnChance;
		public final ModConfigSpec.IntValue caveDustBaseMaxAge;
		public final ModConfigSpec.IntValue caveDustColor;
		public final ModConfigSpec.IntValue caveDustFadeDuration;
		public final ModConfigSpec.DoubleValue caveDustMaxAcceleration;
		public final ModConfigSpec.IntValue caveDustAccelChangeChance;
		public final ModConfigSpec.ConfigValue<List<? extends String>> caveDustExcludeBiomes;

		CommonConfig(ModConfigSpec.Builder builder) {
			builder.comment("Particular Mod Configuration").push("general");

			// Enabled Effects Section
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
			builder.pop();

			// Advanced Settings Section
			builder.comment("Advanced Particle Settings").push("advancedSettings");

			// Firefly Settings
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
			builder.pop(); // frequency modifiers
			builder.pop(); // firefly settings

			// Falling Leaves Settings
			builder.push("fallingLeavesSettings");
			fallingLeavesSpawnChance = builder.comment("Chance of spawning falling leaves (higher = less frequent)")
					.defineInRange("spawnChance", 60, 1, Integer.MAX_VALUE);
			fallingLeavesSpawnRipples = builder.comment("Whether falling leaves create ripples when landing on water")
					.define("spawnRipples", true);
			fallingLeavesLayFlatOnGround = builder.comment("Whether falling leaves lay flat on the ground")
					.define("layFlatOnGround", true);
			fallingLeavesLayFlatRightAngles = builder.comment("Whether falling leaves lay at right angles")
					.define("layFlatRightAngles", false);
			builder.pop(); // falling leaves settings

			// Cave Dust Settings
			builder.push("caveDustSettings");
			caveDustSpawnChance = builder.comment("Chance of spawning cave dust (higher = less frequent)")
					.defineInRange("spawnChance", 700, 1, Integer.MAX_VALUE);
			caveDustBaseMaxAge = builder.comment("Base maximum age of cave dust particles")
					.defineInRange("baseMaxAge", 200, 1, Integer.MAX_VALUE);
			caveDustColor = builder.comment("Color of cave dust particles (RGB hex)")
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
			builder.pop(); // cave dust settings

			builder.pop(); // advanced settings
			builder.pop(); // general
		}
	}

	// Helper methods to access config values
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
	public static boolean cakeEatingParticles() { return COMMON.cakeEatingParticles.get(); }
	public static boolean emissiveLavaDrips() { return COMMON.emissiveLavaDrips.get(); }

	public static boolean rainRipples() {
		if (IrisCompat.areShadersEnabled()) {
			return false; // Override: Disable if shaders are on
		}
		return COMMON.rainRipples.get();
	}

	public static boolean waterDripRipples() {
		if (IrisCompat.areShadersEnabled()) {
			return false; // Override: Disable if shaders are on
		}
		return COMMON.waterDripRipples.get();
	}


	// Helper methods to convert biome string list to ResourceLocation list
	public static List<ResourceLocation> getCaveDustExcludeBiomes() {
		return COMMON.caveDustExcludeBiomes.get().stream()
				.map(ResourceLocation::parse)
				.toList();
	}
}