package com.leclowndu93150.particular;

import com.leclowndu93150.particular.utils.IrisCompat;
import net.minecraft.resources.ResourceLocation;
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

	public static class CommonConfig {
		public final ModConfigSpec.BooleanValue waterSplash;
		public final ModConfigSpec.BooleanValue cascades;
		public final ModConfigSpec.BooleanValue cascadeMist;
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
		public final ModConfigSpec.BooleanValue lavaSplash;
		public final ModConfigSpec.BooleanValue sableSplashes;
		public final ModConfigSpec.BooleanValue screenShake;
		public final ModConfigSpec.BooleanValue leafSnow;
		public final ModConfigSpec.BooleanValue coldBreath;
		public final ModConfigSpec.BooleanValue footstepDust;
		public final ModConfigSpec.BooleanValue pollen;
		public final ModConfigSpec.BooleanValue spores;
		public final ModConfigSpec.BooleanValue embers;
		public final ModConfigSpec.BooleanValue tumbleweeds;
		public final ModConfigSpec.BooleanValue rainbows;

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
		public final ModConfigSpec.DoubleValue fireflyCustomBlockFrequency;

		public final ModConfigSpec.IntValue fallingLeavesSpawnChance;
		public final ModConfigSpec.BooleanValue fallingLeavesSpawnRipples;
		public final ModConfigSpec.BooleanValue fallingLeavesLayFlatOnGround;
		public final ModConfigSpec.BooleanValue fallingLeavesLayFlatRightAngles;

		public final ModConfigSpec.DoubleValue waterSplashMinFallDistance;
		public final ModConfigSpec.BooleanValue waterSplashSmallDroplets;
		public final ModConfigSpec.DoubleValue waterSplashOpacity;
		public final ModConfigSpec.BooleanValue waterSplashSoftEntryParticles;
		public final ModConfigSpec.BooleanValue cuboidWaterfallSpray;
		public final ModConfigSpec.BooleanValue cuboidSplashDroplets;
		public final ModConfigSpec.BooleanValue waterCuboidBiomeTint;
		public final ModConfigSpec.IntValue waterCuboidColor;
		public final ModConfigSpec.IntValue lavaCuboidColor;

		public final ModConfigSpec.BooleanValue cascadeHeightScaling;
		public final ModConfigSpec.DoubleValue cascadeMinIntensity;
		public final ModConfigSpec.DoubleValue cascadeMaxIntensity;
		public final ModConfigSpec.IntValue cascadeMaxFallHeight;
		public final ModConfigSpec.DoubleValue cascadeFoamDensity;
		public final ModConfigSpec.DoubleValue cascadePuffSize;
		public final ModConfigSpec.DoubleValue cascadeMistDensity;

		public final ModConfigSpec.IntValue leafSnowSpawnChance;
		public final ModConfigSpec.BooleanValue coldBreathFirstPerson;
		public final ModConfigSpec.BooleanValue coldBreathMobs;
		public final ModConfigSpec.DoubleValue pollenFrequency;
		public final ModConfigSpec.DoubleValue sporeFrequency;
		public final ModConfigSpec.DoubleValue mushroomSporeFrequency;
		public final ModConfigSpec.IntValue lushCaveSporeChance;
		public final ModConfigSpec.BooleanValue campfireEmbers;
		public final ModConfigSpec.BooleanValue furnaceEmbers;
		public final ModConfigSpec.DoubleValue emberAmount;
		public final ModConfigSpec.IntValue rainbowDuration;
		public final ModConfigSpec.DoubleValue rainbowChance;
		public final ModConfigSpec.DoubleValue rainbowOpacity;
		public final ModConfigSpec.DoubleValue tumbleweedFrequency;
		public final ModConfigSpec.DoubleValue tumbleweedStormBoost;
		public final ModConfigSpec.BooleanValue tumbleweedStormsOnly;
		public final ModConfigSpec.ConfigValue<String> tumbleweedGroundTag;
		public final ModConfigSpec.DoubleValue tumbleweedGravity;
		public final ModConfigSpec.DoubleValue tumbleweedRotation;
		public final ModConfigSpec.DoubleValue tumbleweedBounciness;
		public final ModConfigSpec.BooleanValue footstepDustMobs;
		public final ModConfigSpec.BooleanValue footstepDustReplaceSprint;
		public final ModConfigSpec.BooleanValue footstepDustReplaceLanding;
		public final ModConfigSpec.ConfigValue<List<? extends String>> footstepDustBlocks;

		public final ModConfigSpec.DoubleValue screenShakeIntensity;
		public final ModConfigSpec.ConfigValue<List<? extends String>> screenShakeSounds;

		public final ModConfigSpec.ConfigValue<List<? extends String>> customWaterFluids;
		public final ModConfigSpec.ConfigValue<List<? extends String>> cascadeFluidPairs;
		public final ModConfigSpec.ConfigValue<List<? extends String>> fireflyBiomes;
		public final ModConfigSpec.ConfigValue<List<? extends String>> fireflySpawnBlocks;
		public final ModConfigSpec.ConfigValue<List<? extends String>> fireflyBiomeColors;
		public final ModConfigSpec.ConfigValue<List<? extends String>> fireflyColorPool;

		public final ModConfigSpec.IntValue caveDustSpawnChance;
		public final ModConfigSpec.IntValue caveDustBaseMaxAge;
		public final ModConfigSpec.IntValue caveDustColor;
		public final ModConfigSpec.IntValue caveDustFadeDuration;
		public final ModConfigSpec.DoubleValue caveDustMaxAcceleration;
		public final ModConfigSpec.IntValue caveDustAccelChangeChance;
		public final ModConfigSpec.ConfigValue<List<? extends String>> caveDustExcludeBiomes;

		CommonConfig(ModConfigSpec.Builder builder) {
			builder.comment("Particular Mod Configuration").push("general");

			builder.comment("Enable/Disable Effects").push("enabledEffects");
			waterSplash = builder.comment("Enable water splash particles").define("waterSplash", true);
			cascades = builder.comment("Enable cascade particles").define("cascades", true);
			cascadeMist = builder.comment("Enable the translucent mist that drifts off cascades").define("cascadeMist", true);
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
			sableSplashes = builder.comment("Enable water splash and wake particles for Sable sub-levels (ships, boats, vehicles)").define("sableSplashes", true);
			screenShake = builder.comment("Enable camera shake for loud nearby sounds such as explosions and Warden roars").define("screenShake", true);
			leafSnow = builder.comment("Enable snow falling off leaves that have snow on top or while it is snowing").define("leafSnow", true);
			coldBreath = builder.comment("Enable visible breath from players and mobs in cold places").define("coldBreath", true);
			footstepDust = builder.comment("Enable dust kicked up when running or landing on loose ground such as sand, dirt, gravel and snow").define("footstepDust", true);
			pollen = builder.comment("Enable pollen drifting off flowers during the day").define("pollen", true);
			spores = builder.comment("Enable spores drifting off moss and mushrooms").define("spores", true);
			embers = builder.comment("Enable glowing embers rising from campfires and spitting from lit furnaces").define("embers", true);
			tumbleweeds = builder.comment("Enable bushes tumbling across dry sandy biomes").define("tumbleweeds", true);
			rainbows = builder.comment("Enable a rainbow in the sky after it stops raining during the day").define("rainbows", true);
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

			builder.push("cascadeSettings");
			cascadeHeightScaling = builder.comment("Whether cascades get stronger the further the water falls")
					.define("heightScaling", true);
			cascadeMinIntensity = builder.comment("Cascade intensity for a 1 block fall (1.0 = normal)")
					.defineInRange("minIntensity", 0.4, 0.0, 5.0);
			cascadeMaxIntensity = builder.comment("Cascade intensity for a fall of maxFallHeight blocks or more (1.0 = normal)")
					.defineInRange("maxIntensity", 1.6, 0.0, 5.0);
			cascadeMaxFallHeight = builder.comment("Fall height (in blocks) at which a cascade reaches its maximum intensity")
					.defineInRange("maxFallHeight", 16, 1, 64);
			cascadeFoamDensity = builder.comment("Multiplier for the amount of cascade foam")
					.defineInRange("foamDensity", 1.0, 0.1, 4.0);
			cascadePuffSize = builder.comment("Multiplier for the size of cascade foam puffs")
					.defineInRange("puffSize", 1.0, 0.25, 4.0);
			cascadeMistDensity = builder.comment("Multiplier for the amount of cascade mist")
					.defineInRange("mistDensity", 1.0, 0.0, 2.0);
			builder.pop();

			builder.push("leafSnowSettings");
			leafSnowSpawnChance = builder.comment("Chance of snow falling off a leaf block (higher = less frequent). Twice as frequent while it is snowing")
					.defineInRange("spawnChance", 30, 1, Integer.MAX_VALUE);
			builder.pop();

			builder.push("coldBreathSettings");
			coldBreathFirstPerson = builder.comment("Whether you see your own breath in first person")
					.define("firstPersonBreath", true);
			coldBreathMobs = builder.comment("Whether mobs breathe visibly as well as players")
					.define("mobBreath", true);
			builder.pop();

			builder.push("pollenAndSporeSettings");
			pollenFrequency = builder.comment("Chance for a flower to release pollen each time it is ticked (0 = never, 1 = always)")
					.defineInRange("pollenFrequency", 0.05, 0.0, 1.0);
			sporeFrequency = builder.comment("Chance for a moss block to release a spore each time it is ticked (0 = never, 1 = always)")
					.defineInRange("sporeFrequency", 0.03, 0.0, 1.0);
			mushroomSporeFrequency = builder.comment("Chance for a mushroom to release a spore each time it is ticked (0 = never, 1 = always)")
					.defineInRange("mushroomSporeFrequency", 0.2, 0.0, 1.0);
			lushCaveSporeChance = builder.comment("Chance of a spore appearing in open air in lush caves (higher = less frequent)")
					.defineInRange("lushCaveSporeChance", 150, 1, Integer.MAX_VALUE);
			builder.pop();

			builder.push("emberSettings");
			campfireEmbers = builder.comment("Whether lit campfires and soul campfires give off embers")
					.define("campfireEmbers", true);
			furnaceEmbers = builder.comment("Whether lit furnaces, blast furnaces and smokers spit embers")
					.define("furnaceEmbers", true);
			emberAmount = builder.comment("Multiplier for the number of embers")
					.defineInRange("emberAmount", 1.0, 0.0, 3.0);
			builder.pop();

			builder.push("rainbowSettings");
			rainbowDuration = builder.comment("How long a rainbow stays in the sky, in seconds")
					.defineInRange("rainbowDuration", 240, 10, 3600);
			rainbowChance = builder.comment("Chance for a rainbow to appear when rain stops (0 = never, 1 = always)")
					.defineInRange("rainbowChance", 1.0, 0.0, 1.0);
			rainbowOpacity = builder.comment("Opacity of the rainbow")
					.defineInRange("rainbowOpacity", 0.55, 0.0, 1.0);
			builder.pop();

			builder.push("tumbleweedSettings");
			tumbleweedFrequency = builder.comment("Chance per tick for a tumbling bush to appear near you in a dry sandy biome")
					.defineInRange("tumbleweedFrequency", 0.25, 0.0, 1.0);
			tumbleweedStormBoost = builder.comment("Multiplier for the chance while the weather is stormy")
					.defineInRange("stormBoost", 4.0, 1.0, 50.0);
			tumbleweedStormsOnly = builder.comment("Whether tumbling bushes only appear while the weather is stormy")
					.define("stormsOnly", false);
			tumbleweedGroundTag = builder.comment("Block tag the ground must have for tumbling bushes to appear")
					.define("groundTag", "minecraft:camel_sand_step_sound_blocks");
			tumbleweedGravity = builder.comment("Gravity of tumbling bushes")
					.defineInRange("tumbleweedGravity", 0.2, 0.0, 10.0);
			tumbleweedRotation = builder.comment("How fast tumbling bushes roll")
					.defineInRange("rotationAmount", 0.2, 0.0, 10.0);
			tumbleweedBounciness = builder.comment("How high tumbling bushes bounce off the ground")
					.defineInRange("bounciness", 0.2, 0.0, 10.0);
			builder.pop();

			builder.push("footstepDustSettings");
			footstepDustMobs = builder.comment("Whether mobs kick up dust as well as players")
					.define("mobDust", true);
			footstepDustReplaceSprint = builder.comment("Whether dust replaces the vanilla block chips thrown up when sprinting on dusty blocks")
					.define("replaceSprintParticles", true);
			footstepDustReplaceLanding = builder.comment("Whether dust replaces the vanilla block chips thrown up when landing from a fall on dusty blocks")
					.define("replaceLandingParticles", true);
			footstepDustBlocks = builder.comment("Blocks that produce footstep dust. Use a block id or a #tag. Example: \"minecraft:gravel\", \"#minecraft:sand\"")
					.defineList("dustBlocks", Arrays.asList(
									"#minecraft:sand",
									"#minecraft:snow",
									"#minecraft:concrete_powder",
									"minecraft:gravel",
									"minecraft:dirt",
									"minecraft:grass_block",
									"minecraft:podzol",
									"minecraft:mycelium",
									"minecraft:coarse_dirt",
									"minecraft:rooted_dirt",
									"minecraft:farmland",
									"minecraft:dirt_path",
									"minecraft:soul_sand",
									"minecraft:soul_soil",
									"minecraft:suspicious_sand",
									"minecraft:suspicious_gravel"),
							entry -> entry instanceof String);
			builder.pop();

			builder.push("screenShakeSettings");
			screenShakeIntensity = builder.comment("Multiplier for the strength of all camera shake")
					.defineInRange("shakeIntensity", 1.0, 0.0, 3.0);
			screenShakeSounds = builder.comment("Sounds that shake the camera. Format: \"soundId|intensity|durationTicks|radius\". Example: \"minecraft:entity.generic.explode|0.8|12|16\"")
					.defineList("shakeSounds", Arrays.asList(
									"minecraft:entity.warden.roar|0.6|70|24",
									"minecraft:entity.warden.sonic_boom|1.0|15|24",
									"minecraft:entity.ravager.roar|0.5|25|12",
									"minecraft:entity.ender_dragon.growl|0.4|40|48",
									"minecraft:entity.generic.explode|0.8|12|16",
									"minecraft:entity.dragon_fireball.explode|0.5|12|16"),
							entry -> entry instanceof String);
			builder.pop();

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
	public static boolean cakeEatingParticles() { return COMMON.cakeEatingParticles.get(); }
	public static boolean emissiveLavaDrips() { return COMMON.emissiveLavaDrips.get(); }
	public static boolean lavaSplash() { return COMMON.lavaSplash.get(); }
	public static boolean sableSplashes() { return COMMON.sableSplashes.get(); }
	public static boolean screenShake() { return COMMON.screenShake.get(); }
	public static boolean leafSnow() { return COMMON.leafSnow.get(); }
	public static boolean coldBreath() { return COMMON.coldBreath.get(); }
	public static boolean footstepDust() { return COMMON.footstepDust.get(); }
	public static boolean pollen() { return COMMON.pollen.get(); }
	public static boolean spores() { return COMMON.spores.get(); }
	public static boolean embers() { return COMMON.embers.get(); }
	public static boolean tumbleweeds() { return COMMON.tumbleweeds.get(); }
	public static boolean rainbows() { return COMMON.rainbows.get(); }

	public static boolean rainRipples() {
		if (IrisCompat.areShadersEnabled()) {
			return false;
		}
		return COMMON.rainRipples.get();
	}

	public static boolean waterDripRipples() {
		if (IrisCompat.areShadersEnabled()) {
			return false;
		}
		return COMMON.waterDripRipples.get();
	}

	public static List<ResourceLocation> getCaveDustExcludeBiomes() {
		return COMMON.caveDustExcludeBiomes.get().stream()
				.map(ResourceLocation::parse)
				.toList();
	}
}
