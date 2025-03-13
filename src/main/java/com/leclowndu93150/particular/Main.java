package com.leclowndu93150.particular;

import com.leclowndu93150.particular.compat.RegionsUnexplored;
import com.leclowndu93150.particular.compat.Traverse;
import com.leclowndu93150.particular.compat.WilderWild;
import com.leclowndu93150.particular.mixin.AccessorBiome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Mod(Main.MOD_ID)
public class Main {
	public static final String MOD_ID = "particular";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation currentDimension;
	public static ConcurrentHashMap<BlockPos, Integer> cascades = new ConcurrentHashMap<>();
	private static float fireflyFrequency = 1f;

	private static Map<Block, LeafData> leavesData = new HashMap<>();

	public Main() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		LOGGER.info("I am quite particular about the effects I choose to add :3");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ParticularConfig.COMMON_SPEC);
		Particles.register(modEventBus);
		modEventBus.addListener(this::clientSetup);
		modEventBus.addListener(Particles::registerFactories);
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		// Populate leaves data
		leavesData.put(Blocks.OAK_LEAVES, new LeafData(Particles.OAK_LEAF.get()));
		leavesData.put(Blocks.BIRCH_LEAVES, new LeafData(Particles.BIRCH_LEAF.get(), new Color(FoliageColor.getBirchColor())));
		leavesData.put(Blocks.SPRUCE_LEAVES, new LeafData(Particles.SPRUCE_LEAF.get(), new Color(FoliageColor.getEvergreenColor())));
		leavesData.put(Blocks.JUNGLE_LEAVES, new LeafData(Particles.JUNGLE_LEAF.get()));
		leavesData.put(Blocks.ACACIA_LEAVES, new LeafData(Particles.ACACIA_LEAF.get()));
		leavesData.put(Blocks.DARK_OAK_LEAVES, new LeafData(Particles.DARK_OAK_LEAF.get()));
		leavesData.put(Blocks.AZALEA_LEAVES, new LeafData(Particles.AZALEA_LEAF.get(), Color.white));
		leavesData.put(Blocks.FLOWERING_AZALEA_LEAVES, new LeafData(Particles.AZALEA_LEAF.get(), Color.white));
		leavesData.put(Blocks.MANGROVE_LEAVES, new LeafData(Particles.MANGROVE_LEAF.get()));
		leavesData.put(Blocks.CHERRY_LEAVES, new LeafData(null));

		// Mod compat
		if (ModList.get().isLoaded("traverse")) {
			Traverse.addLeaves();
		}

		if (ModList.get().isLoaded("regions_unexplored")) {
			RegionsUnexplored.addLeaves();
		}

		if (ModList.get().isLoaded("wilderwild")) {
			WilderWild.addLeaves();
		}
	}

	public static void registerLeafData(Block block, LeafData leafData) {
		leavesData.put(block, leafData);
	}

	public static void registerLeafData(ResourceLocation id, LeafData leafData) {
		BuiltInRegistries.BLOCK.getOptional(id).ifPresent(block -> leavesData.put(block, leafData));
	}

	public static LeafData getLeafData(Block block) {
		return leavesData.getOrDefault(block, new Main.LeafData(Particles.OAK_LEAF.get()));
	}

	public static class LeafData {
		private final ParticleOptions particle;
		private final BiFunction<Level, BlockPos, Color> colorBiFunc;

		public LeafData(ParticleOptions particle, BiFunction<Level, BlockPos, Color> colorBiFunc) {
			this.particle = particle;
			this.colorBiFunc = colorBiFunc;
		}

		public LeafData(ParticleOptions particle, Color color) {
			this(particle, (world, pos) -> color);
		}

		public LeafData(ParticleOptions particle) {
			this(particle, (world, pos) -> new Color(BiomeColors.getAverageFoliageColor(world, pos)));
		}

		public ParticleOptions getParticle() {
			return particle;
		}

		public Color getColor(Level world, BlockPos pos) {
			return colorBiFunc.apply(world, pos);
		}
	}

	public static void updateCascade(Level world, BlockPos pos, FluidState state) {
		if (state.is(Fluids.WATER) &&
				world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) &&
				world.getFluidState(pos.below()).is(Fluids.WATER)) {
			int strength = 0;
			if (world.getFluidState(pos.north()).is(Fluids.WATER)) { ++strength; }
			if (world.getFluidState(pos.east()).is(Fluids.WATER)) { ++strength; }
			if (world.getFluidState(pos.south()).is(Fluids.WATER)) { ++strength; }
			if (world.getFluidState(pos.west()).is(Fluids.WATER)) { ++strength; }

			if (strength > 0) {
				// Check if encased
				if (!world.getBlockState(pos.above().north()).isAir() &&
						!world.getBlockState(pos.above().east()).isAir() &&
						!world.getBlockState(pos.above().south()).isAir() &&
						!world.getBlockState(pos.above().west()).isAir()) {
					return;
				}

				// This wouldn't be needed in Rust
				pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

				if (cascades.contains(pos)) {
					cascades.replace(pos, strength);
				} else {
					cascades.put(pos, strength);
				}
			} else {
				cascades.remove(pos);
			}
		} else {
			cascades.remove(pos);
		}
	}

	public static void spawnBubble(ParticleOptions particle, Level world, BlockPos pos) {
		double x = pos.getX() + 0.25d + world.random.nextDouble() * 0.5d;
		double y = pos.getY() + 0.25d + world.random.nextDouble() * 0.5d;
		double z = pos.getZ() + 0.25d + world.random.nextDouble() * 0.5d;

		world.addParticle(particle, x, y, z, 0, 0, 0);
	}

	public static void spawnFirefly(Level world, BlockPos pos, RandomSource random) {
		if (random.nextDouble() > fireflyFrequency) {
			return;
		}

		Biome biome = world.getBiome(pos).value();
		float downfall = ((AccessorBiome)(Object) biome).getWeather().downfall();
		if ((!world.isRaining() || ParticularConfig.COMMON.fireflyCanSpawnInRain.get()) &&
				random.nextInt(30 - (int)(10 * downfall)) == 0) {
			long time = world.getDayTime();
			float temp = biome.getBaseTemperature();
			if (time >= ParticularConfig.COMMON.fireflyStartTime.get() &&
					time <= ParticularConfig.COMMON.fireflyEndTime.get() &&
					temp >= ParticularConfig.COMMON.fireflyMinTemp.get() &&
					temp <= ParticularConfig.COMMON.fireflyMaxTemp.get()) {
				world.addParticle(Particles.FIREFLY.get(), pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
			}
		}
	}

	public static void spawnDoubleBubbles(ParticleOptions particle, Level world, BlockPos pos, BlockState state) {
		ChestType chestType = state.getValue(BlockStateProperties.CHEST_TYPE);

		int xLen = 0;
		int zLen = 0;
		int xOffset = 0;
		int zOffset = 0;

		switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case NORTH -> {
				xLen = 1;

				if (chestType == ChestType.RIGHT) {
					xOffset = -1;
				}
			}
			case SOUTH -> {
				xLen = 1;

				if (chestType == ChestType.LEFT) {
					xOffset = -1;
				}
			}
			case EAST -> {
				zLen = 1;

				if (chestType == ChestType.RIGHT) {
					zOffset = -1;
				}
			}
			case WEST -> {
				zLen = 1;

				if (chestType == ChestType.LEFT) {
					zOffset = -1;
				}
			}
		}

		for (int i = 0; i < 2; ++i) {
			double x = pos.getX() + 0.25d + world.random.nextDouble() * (0.5d + xLen) + xOffset;
			double y = pos.getY() + 0.25d + world.random.nextDouble() * 0.5d;
			double z = pos.getZ() + 0.25d + world.random.nextDouble() * (0.5d + zLen) + zOffset;

			world.addParticle(particle, x, y, z, 0, 0, 0);
		}
	}

	public static void spawnChestBubbles(ParticleOptions particle, Level world, BlockPos pos) {
		for (int i = 0; i < 10; ++i) {
			spawnBubble(particle, world, pos);
		}
	}

	public static void spawnDoubleChestBubbles(ParticleOptions particle, Level world, BlockPos pos, BlockState state) {
		ChestType chestType = state.getValue(BlockStateProperties.CHEST_TYPE);

		int xLen = 0;
		int zLen = 0;
		int xOffset = 0;
		int zOffset = 0;

		switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case NORTH -> {
				xLen = 1;

				if (chestType == ChestType.RIGHT) {
					xOffset = -1;
				}
			}
			case SOUTH -> {
				xLen = 1;

				if (chestType == ChestType.LEFT) {
					xOffset = -1;
				}
			}
			case EAST -> {
				zLen = 1;

				if (chestType == ChestType.RIGHT) {
					zOffset = -1;
				}
			}
			case WEST -> {
				zLen = 1;

				if (chestType == ChestType.LEFT) {
					zOffset = -1;
				}
			}
		}

		for (int i = 0; i < 20; ++i) {
			double x = pos.getX() + 0.25d + world.random.nextDouble() * (0.5d + xLen) + xOffset;
			double y = pos.getY() + 0.25d + world.random.nextDouble() * 0.5d;
			double z = pos.getZ() + 0.25d + world.random.nextDouble() * (0.5d + zLen) + zOffset;

			world.addParticle(particle, x, y, z, 0, 0, 0);
		}
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
	public static class ClientEvents {

		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			Level world = Minecraft.getInstance().level;
			if (world == null || event.phase != TickEvent.Phase.START) return;

			RandomSource random = world.random;

			// Set firefly frequency
			if (world.getDayTime() == ParticularConfig.COMMON.fireflyStartTime.get()) {
				var dailyRandomList = ParticularConfig.COMMON.fireflyDailyRandom.get();
				fireflyFrequency = dailyRandomList.get(random.nextInt(dailyRandomList.size())).floatValue();
			}

			// Cascades
			if (!ParticularConfig.waterSplash()) return;

			cascades.forEach((pos, strength) -> {
				float height = world.getFluidState(pos.above()).getOwnHeight();
				double x = pos.getX();
				double y = (double) pos.getY() + random.nextDouble() * height + 1;
				double z = pos.getZ();

				if (random.nextBoolean()) {
					x += random.nextDouble();
					z += random.nextIntBetweenInclusive(0, 1);
				} else {
					x += random.nextIntBetweenInclusive(0, 1);
					z += random.nextDouble();
				}

				Particle cascade = Minecraft.getInstance().particleEngine.createParticle(Particles.CASCADE.get(), x, y, z, 0, 0, 0);
				if (cascade != null) {
					float size = strength / 4f * height;
					cascade.scale(1f - (1f - size) / 2f);
				}
			});
		}

		@SubscribeEvent
		public static void onChunkLoad(ChunkEvent.Load event) {
			Level world = (Level) event.getLevel();
			if (!ParticularConfig.cascades() || !world.isClientSide()) return;

			// Changing dimensions doesn't count as unloading chunks so I need to do this test
			ResourceLocation newDimension = world.dimensionType().effectsLocation();
			if (newDimension != currentDimension) {
				currentDimension = newDimension;
				cascades.clear();
			}

			var chunk = event.getChunk();
			// Iterate through blocks in the chunk to find water
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = world.getMinBuildHeight(); y < world.getMaxBuildHeight(); y++) {
						mutablePos.set(chunk.getPos().getMinBlockX() + x, y, chunk.getPos().getMinBlockZ() + z);
						BlockState blockState = chunk.getBlockState(mutablePos);
						if (blockState.getFluidState().is(Fluids.WATER)) {
							updateCascade(world, mutablePos.immutable(), blockState.getFluidState());
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void onChunkUnload(ChunkEvent.Unload event) {
			if (!ParticularConfig.cascades() || !event.getLevel().isClientSide()) return;

			var chunkPos = event.getChunk().getPos();
			cascades.entrySet().removeIf(entry -> {
				BlockPos pos = entry.getKey();
				return chunkPos.getMinBlockX() <= pos.getX() && pos.getX() < chunkPos.getMinBlockX() + 16 &&
						chunkPos.getMinBlockZ() <= pos.getZ() && pos.getZ() < chunkPos.getMinBlockZ() + 16;
			});
		}

		@SubscribeEvent
		public static void onLevelUnload(LevelEvent.Unload event) {
			if (event.getLevel().isClientSide()) {
				cascades.clear();
			}
		}
	}
}