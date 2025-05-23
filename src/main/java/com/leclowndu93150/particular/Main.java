package com.leclowndu93150.particular;

import com.leclowndu93150.particular.mixin.AccessorBiome;
import com.leclowndu93150.particular.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

@Mod(Main.MOD_ID)
public class Main {
	public static final String MOD_ID = "particular";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation currentDimension;
	public static ConcurrentHashMap<BlockPos, Integer> cascades = new ConcurrentHashMap<>();
	public static float fireflyFrequency = 1f;

	public Main() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		LOGGER.info("I am quite particular about the effects I choose to add :3");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ParticularConfig.COMMON_SPEC);
		Particles.register(modEventBus);
		modEventBus.addListener(this::commonSetup);
		//modEventBus.addListener(this::clientSetup);
		//modEventBus.addListener(Particles::registerFactories);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(NetworkHandler::init);
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

					cascades.put(cascadePos, strength);
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

}