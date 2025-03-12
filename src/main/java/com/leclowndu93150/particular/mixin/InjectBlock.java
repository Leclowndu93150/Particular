package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class InjectBlock
{
	@Unique
	private static boolean isValidBiome(Holder<Biome> biome)
	{
		var key = biome.unwrapKey();
		return key.map(biomeRegistryKey -> !ParticularConfig.COMMON.caveDustExcludeBiomes.get().contains(biomeRegistryKey.location())).orElse(true);
	}

	@Inject(at = @At("TAIL"), method = "animateTick")
	public void spawnParticles(BlockState state, Level world, BlockPos pos, RandomSource random, CallbackInfo ci)
	{
		Block block = state.getBlock();

		if (ParticularConfig.fireflies())
		{
			// Fireflies
			double val = random.nextDouble();
			if ((block == Blocks.GRASS_BLOCK && val < ParticularConfig.COMMON.fireflyGrassFrequency.get()) ||
				(block == Blocks.TALL_GRASS && val < ParticularConfig.COMMON.fireflyTallGrassFrequency.get()) ||
				(block instanceof FlowerBlock && val < ParticularConfig.COMMON.fireflyFlowersFrequency.get()) ||
				(block instanceof TallFlowerBlock && val < ParticularConfig.COMMON.fireflyTallFlowersFrequency.get()))
			{
				Main.spawnFirefly(world, pos, random);
				return;
			}
		}

		if (ParticularConfig.caveDust())
		{
			// Cave dust
			if (block == Blocks.AIR || block == Blocks.CAVE_AIR)
			{
				if (random.nextInt(ParticularConfig.COMMON.caveDustSpawnChance.get()) == 0 && pos.getY() < world.getSeaLevel() && isValidBiome(world.getBiome(pos)))
				{
					float lightChance = 1f - Math.min(8, world.getBrightness(LightLayer.SKY, pos)) / 8f;
					float depthChance = Math.min(1f, (world.getSeaLevel() - pos.getY()) / 96f);

					if (random.nextFloat() < lightChance * depthChance)
					{
						double x = (double)pos.getX() + random.nextDouble();
						double y = (double)pos.getY() + random.nextDouble();
						double z = (double)pos.getZ() + random.nextDouble();
						world.addParticle(Particles.CAVE_DUST.get(), x, y, z, 0.0, 0.0, 0.0);
					}
				}
			}
		}
	}
}