package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AmbientMotes {
    private static final long DAY_END = 12000L;
    private static final double DRIFT = 0.006;

    private static final int[] POLLEN = {0xFFEC82, 0xFFF6B0, 0xFADE68, 0xFFFCD6};
    private static final int[] MOSS_SPORES = {0xC4E296, 0xD6ECB2, 0xB0D084};
    private static final int[] BROWN_SPORES = {0xC4A07E, 0xDEC4AA};
    private static final int[] RED_SPORES = {0xEBD7CD, 0xF4E6DE};

    public static void onAnimateTick(Level world, BlockPos pos, BlockState state, RandomSource random) {
        if (state.isAir()) {
            if (!ParticularConfig.spores() || random.nextInt(ParticularConfig.COMMON.lushCaveSporeChance.get()) != 0) return;
            if (!world.getBiome(pos).is(Biomes.LUSH_CAVES)) return;

            spawn(random, MOSS_SPORES,
                    pos.getX() + random.nextDouble(),
                    pos.getY() + random.nextDouble(),
                    pos.getZ() + random.nextDouble(),
                    random.nextDouble() * 0.006 - 0.003);
            return;
        }

        Block block = state.getBlock();

        if (block instanceof FlowerBlock || block instanceof TallFlowerBlock) {
            if (!ParticularConfig.pollen() || random.nextDouble() >= ParticularConfig.COMMON.pollenFrequency.get()) return;
            if (world.getOverworldClockTime() % 24000L >= DAY_END || world.isRainingAt(pos.above())) return;

            spawn(random, POLLEN,
                    pos.getX() + 0.3 + random.nextDouble() * 0.4,
                    pos.getY() + 0.5 + random.nextDouble() * 0.4,
                    pos.getZ() + 0.3 + random.nextDouble() * 0.4,
                    0.004 + random.nextDouble() * 0.006);
            return;
        }

        if (!ParticularConfig.spores()) return;

        if (block == Blocks.MOSS_BLOCK || block == Blocks.MOSS_CARPET) {
            if (random.nextDouble() >= ParticularConfig.COMMON.sporeFrequency.get()) return;

            if (world.getBlockState(pos.above()).isAir()) {
                spawn(random, MOSS_SPORES, pos.getX() + random.nextDouble(), pos.getY() + 1.05, pos.getZ() + random.nextDouble(), 0.002 + random.nextDouble() * 0.004);
            } else if (world.getBlockState(pos.below()).isAir()) {
                spawn(random, MOSS_SPORES, pos.getX() + random.nextDouble(), pos.getY() - 0.05, pos.getZ() + random.nextDouble(), -0.002 - random.nextDouble() * 0.004);
            }
            return;
        }

        boolean brown = block == Blocks.BROWN_MUSHROOM || block == Blocks.BROWN_MUSHROOM_BLOCK;
        if (brown || block == Blocks.RED_MUSHROOM || block == Blocks.RED_MUSHROOM_BLOCK) {
            if (random.nextDouble() >= ParticularConfig.COMMON.mushroomSporeFrequency.get()) return;

            boolean cap = block == Blocks.BROWN_MUSHROOM_BLOCK || block == Blocks.RED_MUSHROOM_BLOCK;
            if (cap && !world.getBlockState(pos.below()).isAir()) return;

            spawn(random, brown ? BROWN_SPORES : RED_SPORES,
                    pos.getX() + 0.2 + random.nextDouble() * 0.6,
                    cap ? pos.getY() - 0.05 : pos.getY() + 0.3,
                    pos.getZ() + 0.2 + random.nextDouble() * 0.6,
                    -0.003 - random.nextDouble() * 0.004);
        }
    }

    private static void spawn(RandomSource random, int[] palette, double x, double y, double z, double lift) {
        Minecraft mc = Minecraft.getInstance();
        ParticleStatus status = mc.options.particles().get();
        if (status == ParticleStatus.MINIMAL || (status == ParticleStatus.DECREASED && random.nextBoolean())) return;
        if (mc.player == null || mc.player.distanceToSqr(x, y, z) > 32.0 * 32.0) return;

        SingleQuadParticle mote = (SingleQuadParticle) mc.particleEngine.createParticle(Particles.MOTE(), x, y, z,
                random.nextDouble() * DRIFT * 2 - DRIFT, lift, random.nextDouble() * DRIFT * 2 - DRIFT);
        if (mote == null) return;

        int colour = palette[random.nextInt(palette.length)];
        mote.setColor((colour >> 16 & 0xFF) / 255f, (colour >> 8 & 0xFF) / 255f, (colour & 0xFF) / 255f);
    }
}
