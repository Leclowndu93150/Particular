package com.leclowndu93150.particular;

import com.leclowndu93150.particular.mixin.AccessorBiome;
import com.leclowndu93150.particular.platform.Services;
import com.leclowndu93150.particular.utils.CascadeCache;
import com.leclowndu93150.particular.utils.CascadeData;
import com.leclowndu93150.particular.utils.LeafColorUtil;
import com.leclowndu93150.particular.utils.TextureCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class CommonClass {
	public static ResourceLocation currentDimension;
	public static ConcurrentHashMap<BlockPos, CascadeData> cascades = new ConcurrentHashMap<>();
	public static float fireflyFrequency = 1f;
	private static Map<Block, LeafData> leavesData = new HashMap<>();

	public static void init() {
		Constants.LOG.info("I am quite particular about the effects I choose to add :3");
	}

	public static void clientSetup() {
		leavesData.put(Blocks.OAK_LEAVES, new LeafData(Particles.OAK_LEAF()));
		leavesData.put(Blocks.BIRCH_LEAVES, new LeafData(Particles.BIRCH_LEAF(), new Color(FoliageColor.getBirchColor())));
		leavesData.put(Blocks.SPRUCE_LEAVES, new LeafData(Particles.SPRUCE_LEAF(), new Color(FoliageColor.getEvergreenColor())));
		leavesData.put(Blocks.JUNGLE_LEAVES, new LeafData(Particles.JUNGLE_LEAF()));
		leavesData.put(Blocks.ACACIA_LEAVES, new LeafData(Particles.ACACIA_LEAF()));
		leavesData.put(Blocks.DARK_OAK_LEAVES, new LeafData(Particles.DARK_OAK_LEAF()));
		leavesData.put(Blocks.AZALEA_LEAVES, new LeafData(Particles.AZALEA_LEAF(), Color.white));
		leavesData.put(Blocks.FLOWERING_AZALEA_LEAVES, new LeafData(Particles.AZALEA_LEAF(), Color.white));
		leavesData.put(Blocks.MANGROVE_LEAVES, new LeafData(Particles.MANGROVE_LEAF()));
		leavesData.put(Blocks.CHERRY_LEAVES, new LeafData(null));

		for (Block block : BuiltInRegistries.BLOCK) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

			if (leavesData.containsKey(block)) {
				continue;
			}

			boolean isLeafBlock = block instanceof LeavesBlock ||
					id.getPath().contains("leaves") ||
					id.getPath().contains("leaf");

			if (isLeafBlock) {
				ParticleOptions particle = Particles.OAK_LEAF();

				if (id.getPath().contains("spruce") || id.getPath().contains("pine") ||
						id.getPath().contains("fir") || id.getPath().contains("conifer")) {
					particle = Particles.SPRUCE_LEAF();
				} else if (id.getPath().contains("birch")) {
					particle = Particles.BIRCH_LEAF();
				} else if (id.getPath().contains("jungle")) {
					particle = Particles.JUNGLE_LEAF();
				} else if (id.getPath().contains("acacia")) {
					particle = Particles.ACACIA_LEAF();
				} else if (id.getPath().contains("dark_oak")) {
					particle = Particles.DARK_OAK_LEAF();
				} else if (id.getPath().contains("mangrove")) {
					particle = Particles.MANGROVE_LEAF();
				}

				LeafData leafData = new LeafData(particle);
				leavesData.put(block, leafData);
			}
		}
	}

	public static void onResourcesReloaded() {
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
			Constants.LOG.error("Failed to extract leaf color", e);
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
		return leavesData.getOrDefault(block, new LeafData(Particles.OAK_LEAF()));
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
		BlockPos immutablePos = pos.immutable();
		if (cascades.containsKey(immutablePos)) {
			return;
		}

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
					cascades.put(immutablePos, new CascadeData(strength, world.getGameTime() - 101));
					CascadeCache.add(world, immutablePos, strength);
				}
			}
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
			long time = world.getDayTime() % 24000;
			float temp = biome.getBaseTemperature();
			if (time >= ParticularConfig.COMMON.fireflyStartTime.get() &&
					time <= ParticularConfig.COMMON.fireflyEndTime.get() &&
					temp >= ParticularConfig.COMMON.fireflyMinTemp.get() &&
					temp <= ParticularConfig.COMMON.fireflyMaxTemp.get()) {
				world.addParticle(Particles.FIREFLY(), pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
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

	public static void onClientTick(Level world) {
		if (world == null) return;

		RandomSource random = world.random;

		long timeOfDay = world.getDayTime() % 24000;
		if (timeOfDay == ParticularConfig.COMMON.fireflyStartTime.get()) {
			var dailyRandomList = ParticularConfig.COMMON.fireflyDailyRandom.get();
			fireflyFrequency = dailyRandomList.get(random.nextInt(dailyRandomList.size())).floatValue();
		}

		if (!ParticularConfig.cascades()) return;

		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
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

			if (chunkDistance > renderDistance + 2) {
				return true;
			}

			if (currentTime - cascadeData.createdTime > 100) {
				if (world.getFluidState(pos).is(Fluids.WATER) &&
						world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) &&
						world.getFluidState(pos.below()).is(Fluids.WATER)) {

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
							cascades.put(pos, new CascadeData(strength, currentTime));
							return false;
						}
					}
				}
				CascadeCache.remove(world, pos);
				return true;
			}

			if (!world.getFluidState(pos).is(Fluids.WATER) ||
					!world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) ||
					!world.getFluidState(pos.below()).is(Fluids.WATER)) {
				CascadeCache.remove(world, pos);
				return true;
			}

			float height = world.getFluidState(pos.above()).getOwnHeight();
			int particles = 2 + cascadeData.strength;
			for (int i = 0; i < particles; i++) {
				double x = pos.getX();
				double y = (double) pos.getY() + random.nextDouble() * height + 1;
				double z = pos.getZ();
				int side = random.nextInt(4);

				if (side == 0 && world.getFluidState(pos.north()).is(Fluids.WATER)) {
					x += random.nextDouble();
					z += 0.5 + (random.nextDouble() * 0.25 - 0.5) * 1.5;
				} else if (side == 1 && world.getFluidState(pos.east()).is(Fluids.WATER)) {
					x += 0.5 + (0.25 + random.nextDouble() * 0.25) * 1.5;
					z += random.nextDouble();
				} else if (side == 2 && world.getFluidState(pos.south()).is(Fluids.WATER)) {
					x += random.nextDouble();
					z += 0.5 + (0.25 + random.nextDouble() * 0.25) * 1.5;
				} else if (world.getFluidState(pos.west()).is(Fluids.WATER)) {
					x += 0.5 + (random.nextDouble() * 0.25 - 0.5) * 1.5;
					z += random.nextDouble();
				} else if (random.nextBoolean()) {
					x += random.nextDouble();
					z += random.nextIntBetweenInclusive(0, 1);
				} else {
					x += random.nextIntBetweenInclusive(0, 1);
					z += random.nextDouble();
				}

				var cascade = mc.particleEngine.createParticle(Particles.CASCADE(), x, y, z, 0, 0, 0);
				if (cascade != null) {
					float size = Math.max(0.75f, cascadeData.strength / 4f * height);
					cascade.scale(1f - (1f - size) / 2f);
				}
			}

			return false;
		});
	}

	public static void onChunkLoad(Level world, ChunkPos chunk) {
		if (!ParticularConfig.cascades() || !world.isClientSide()) return;

		ResourceLocation newDimension = world.dimension().location();
		if (currentDimension != null && !newDimension.equals(currentDimension)) {
			Constants.LOG.debug("Dimension changed from {} to {}, clearing cascades", currentDimension, newDimension);
			cascades.clear();
			CascadeCache.onDimensionChange(world);
		} else {
			CascadeCache.init(world);
		}
		currentDimension = newDimension;

		CascadeCache.getChunk(world, chunk).forEach((pos, strength) -> {
			if (cascades.containsKey(pos)) return;
			if (world.getFluidState(pos).is(Fluids.WATER) &&
					world.getFluidState(pos.above()).is(Fluids.FLOWING_WATER) &&
					world.getFluidState(pos.below()).is(Fluids.WATER)) {
				cascades.put(pos, new CascadeData(strength, world.getGameTime() - 101));
			} else {
				CascadeCache.remove(world, pos);
			}
		});
	}

	public static void onChunkUnload(Level world, int minX, int maxX, int minZ, int maxZ) {
		if (!ParticularConfig.cascades() || !world.isClientSide()) return;

		cascades.entrySet().removeIf(entry -> {
			BlockPos pos = entry.getKey();
			return pos.getX() >= minX && pos.getX() <= maxX &&
					pos.getZ() >= minZ && pos.getZ() <= maxZ;
		});
	}

	public static void onLevelUnload(Level world) {
		if (world.isClientSide()) {
			cascades.clear();
			CascadeCache.onLevelUnload();
		}
	}

	public static void cleanupInvalidCascades(Level world) {
		if (!ParticularConfig.cascades()) {
			cascades.clear();
			return;
		}

		long currentTime = world.getGameTime();

		cascades.entrySet().removeIf(entry -> {
			BlockPos pos = entry.getKey();
			CascadeData cascadeData = entry.getValue();

			if (!world.hasChunkAt(pos)) {
				return true;
			}

			if (currentTime - cascadeData.createdTime > 100) {
				return true;
			}

			FluidState currentState = world.getFluidState(pos);
			FluidState aboveState = world.getFluidState(pos.above());
			FluidState belowState = world.getFluidState(pos.below());

			boolean isValid = currentState.is(Fluids.WATER) &&
					aboveState.is(Fluids.FLOWING_WATER) &&
					belowState.is(Fluids.WATER);

			if (!isValid) {
				CascadeCache.remove(world, pos);
			}
			return !isValid;
		});
	}
}
