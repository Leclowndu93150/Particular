package com.leclowndu93150.particular;

import com.leclowndu93150.particular.compat.RegionsUnexplored;
import com.leclowndu93150.particular.compat.Traverse;
import com.leclowndu93150.particular.compat.WilderWild;
import com.leclowndu93150.particular.mixin.AccessorBiome;
import com.leclowndu93150.particular.utils.CascadeData;
import com.leclowndu93150.particular.utils.LeafColorUtil;
import com.leclowndu93150.particular.utils.TextureCache;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
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
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Mod(Main.MOD_ID)
public class Main {
	public static final String MOD_ID = "particular";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation currentDimension;
	public static ConcurrentHashMap<BlockPos, CascadeData> cascades = new ConcurrentHashMap<>();
	private static float fireflyFrequency = 1f;

	private static Map<Block, LeafData> leavesData = new HashMap<>();

	public Main(IEventBus modEventBus, ModContainer modContainer) {
		LOGGER.info("I am quite particular about the effects I choose to add :3");
		ParticularConfig.register(modContainer);
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

		event.enqueueWork(() -> {
			for (Block block : BuiltInRegistries.BLOCK) {
				ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

				// Skip blocks we've already registered
				if (leavesData.containsKey(block)) {
					continue;
				}

				boolean isLeafBlock = block instanceof LeavesBlock ||
						id.getPath().contains("leaves") ||
						id.getPath().contains("leaf");

				if (isLeafBlock) {

					ParticleOptions particle = Particles.OAK_LEAF.get(); // Default fallback

					if (id.getPath().contains("spruce") || id.getPath().contains("pine") ||
							id.getPath().contains("fir") || id.getPath().contains("conifer")) {
						particle = Particles.SPRUCE_LEAF.get();
					} else if (id.getPath().contains("birch")) {
						particle = Particles.BIRCH_LEAF.get();
					} else if (id.getPath().contains("jungle")) {
						particle = Particles.JUNGLE_LEAF.get();
					} else if (id.getPath().contains("acacia")) {
						particle = Particles.ACACIA_LEAF.get();
					} else if (id.getPath().contains("dark_oak")) {
						particle = Particles.DARK_OAK_LEAF.get();
					} else if (id.getPath().contains("mangrove")) {
						particle = Particles.MANGROVE_LEAF.get();
					}

					LeafData leafData = new LeafData(particle);
					leavesData.put(block, leafData);
				}
			}

		});
	}

	@SubscribeEvent
	public static void onResourcesReloaded(TextureAtlasStitchedEvent event) {
		TextureCache.clear();
	}

	public static Color extractLeafColor(Level world, BlockPos pos, Block block) {
		BlockState state = block.defaultBlockState();
		try {
			if (world.getBlockState(pos).getBlock() == block) {
				state = world.getBlockState(pos);
			}
			double[] colorValues = LeafColorUtil.getBlockTextureColor(state, world, pos);
			return LeafColorUtil.getColorFromValues(colorValues);
		} catch (Exception e) {
			LOGGER.error("Failed to extract leaf color", e);
			return new Color(BiomeColors.getAverageFoliageColor(world, pos));
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
			this(particle, (world, pos) -> {
				Block block = world.getBlockState(pos).getBlock();
				return extractLeafColor(world, pos, block);
			});
		}

		public ParticleOptions getParticle() {
			return particle;
		}

		public Color getColor(Level world, BlockPos pos) {
			return colorBiFunc.apply(world, pos);
		}
	}

	public static void updateCascade(Level world, BlockPos pos, FluidState state) {
		BlockPos cascadePos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

		boolean shouldHaveCascade = state.is(Fluids.WATER) &&
				world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) &&
				world.getFluidState(pos.below()).is(Fluids.WATER);

		if (shouldHaveCascade) {
			int strength = 0;
			if (world.getFluidState(pos.north()).is(Fluids.WATER)) { ++strength; }
			if (world.getFluidState(pos.east()).is(Fluids.WATER)) { ++strength; }
			if (world.getFluidState(pos.south()).is(Fluids.WATER)) { ++strength; }
			if (world.getFluidState(pos.west()).is(Fluids.WATER)) { ++strength; }

			if (strength > 0) {
				boolean isEncased = !world.getBlockState(pos.above().north()).isAir() &&
						!world.getBlockState(pos.above().east()).isAir() &&
						!world.getBlockState(pos.above().south()).isAir() &&
						!world.getBlockState(pos.above().west()).isAir();

				if (!isEncased) {

					int finalStrength = strength;
					cascades.computeIfAbsent(pos, k -> new CascadeData(finalStrength, world.getGameTime()));
				} else {
					cascades.remove(cascadePos);
				}
			} else {
				cascades.remove(cascadePos);
			}
		} else {
			cascades.remove(cascadePos);
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

	@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
	public static class ClientEvents {

		private static int cascadeCleanupTicks = 0;

		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Pre event) {
			Level world = Minecraft.getInstance().level;
			if (world == null) return;

			RandomSource random = world.random;

			if (world.getDayTime() == ParticularConfig.COMMON.fireflyStartTime.get()) {
				var dailyRandomList = ParticularConfig.COMMON.fireflyDailyRandom.get();
				fireflyFrequency = dailyRandomList.get(random.nextInt(dailyRandomList.size())).floatValue();
			}

			if (!ParticularConfig.waterSplash()) return;

			Minecraft mc = Minecraft.getInstance();
			int renderDistance = mc.options.renderDistance().get();
			BlockPos playerPos = mc.player.blockPosition();
			long currentTime = world.getGameTime();

			cascades.entrySet().removeIf(entry -> {
				BlockPos pos = entry.getKey();
				CascadeData cascadeData = entry.getValue();

				int chunkDistance = Math.max(
						Math.abs((pos.getX() >> 4) - (playerPos.getX() >> 4)),
						Math.abs((pos.getZ() >> 4) - (playerPos.getZ() >> 4))
				);

				if (chunkDistance > renderDistance) {
					return true;
				}

				// Remove old cascades (expire after 5 seconds)
				if (currentTime - cascadeData.createdTime > 100) {
					return true;
				}

				if (!world.getFluidState(pos).is(Fluids.WATER) ||
						!world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) ||
						!world.getFluidState(pos.below()).is(Fluids.WATER)) {
					return true;
				}

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

				Particle cascade = mc.particleEngine.createParticle(Particles.CASCADE.get(), x, y, z, 0, 0, 0);
				if (cascade != null) {
					float size = cascadeData.strength / 4f * height;
					cascade.scale(1f - (1f - size) / 2f);
				}

				return false;
			});

			if (++cascadeCleanupTicks >= 100) {
				cascadeCleanupTicks = 0;
				cleanupInvalidCascades(world);
			}
		}

		@SubscribeEvent
		public static void onChunkLoad(ChunkEvent.Load event) {
			Level world = (Level) event.getLevel();
			if (!ParticularConfig.cascades() || !world.isClientSide()) return;

			ResourceLocation newDimension = world.dimensionType().effectsLocation();
			if (currentDimension != null && !newDimension.equals(currentDimension)) {
				Main.LOGGER.debug("Dimension changed from {} to {}, clearing cascades", currentDimension, newDimension);
				cascades.clear();
			}
			currentDimension = newDimension;

			CompletableFuture.runAsync(() -> {
				ChunkAccess chunk = event.getChunk();
				final int minX = chunk.getPos().getMinBlockX();
				final int minZ = chunk.getPos().getMinBlockZ();
				BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

				List<Pair<BlockPos, FluidState>> waterBlocks = new ArrayList<>();

				for (int sectionIndex = 0; sectionIndex < chunk.getSectionsCount(); sectionIndex++) {
					LevelChunkSection section = chunk.getSection(sectionIndex);

					if (section == null || section.hasOnlyAir()) continue;
					if (!section.maybeHas(state -> state.getFluidState().is(Fluids.WATER))) continue;

					int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
					int baseY = sectionY << 4;

					PalettedContainer<BlockState> states = section.getStates();

					for (int y = 0; y < 16; y++) {
						for (int z = 0; z < 16; z++) {
							for (int x = 0; x < 16; x++) {
								BlockState state = states.get(x, y, z);
								FluidState fluidState = state.getFluidState();

								if (fluidState.is(Fluids.WATER)) {
									int worldX = minX + x;
									int worldY = baseY + y;
									int worldZ = minZ + z;

									waterBlocks.add(Pair.of(
											new BlockPos(worldX, worldY, worldZ),
											fluidState
									));
								}
							}
						}
					}
				}

				if (!waterBlocks.isEmpty()) {
					Minecraft.getInstance().execute(() -> {
						for (Pair<BlockPos, FluidState> pair : waterBlocks) {
							Main.updateCascade(world, pair.getFirst(), pair.getSecond());
						}
					});
				}
			}, Util.backgroundExecutor());
		}

		@SubscribeEvent
		public static void onChunkUnload(ChunkEvent.Unload event) {
			if (!ParticularConfig.cascades() || !event.getLevel().isClientSide()) return;

			var chunkPos = event.getChunk().getPos();
			int minX = chunkPos.getMinBlockX();
			int maxX = chunkPos.getMaxBlockX();
			int minZ = chunkPos.getMinBlockZ();
			int maxZ = chunkPos.getMaxBlockZ();

			cascades.entrySet().removeIf(entry -> {
				BlockPos pos = entry.getKey();
				return pos.getX() >= minX && pos.getX() <= maxX &&
						pos.getZ() >= minZ && pos.getZ() <= maxZ;
			});
		}


		@SubscribeEvent
		public static void onLevelUnload(LevelEvent.Unload event) {
			if (event.getLevel().isClientSide()) {
				cascades.clear();
			}
		}
	}

	private static void cleanupInvalidCascades(Level world) {
		if (!ParticularConfig.cascades()) {
			cascades.clear();
			return;
		}

		cascades.entrySet().removeIf(entry -> {
			BlockPos pos = entry.getKey();

			if (!world.hasChunkAt(pos)) {
				return true;
			}

			FluidState currentState = world.getFluidState(pos);
			FluidState aboveState = world.getFluidState(pos.above());
			FluidState belowState = world.getFluidState(pos.below());

			boolean isValid = currentState.is(Fluids.WATER) &&
					aboveState.is(Fluids.FLOWING_WATER) &&
					belowState.is(Fluids.WATER);

			if (!isValid) {
				return true;
			}

			return false;
		});
	}
}