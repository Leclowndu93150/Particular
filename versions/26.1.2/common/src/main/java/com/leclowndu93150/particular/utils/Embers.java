package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.particles.EmberParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class Embers {
    private static final double RANGE_SQ = 32.0 * 32.0;
    private static final int CAMPFIRE_STAGGER = 40;
    private static final int FURNACE_STAGGER = 20;
    private static final float FURNACE_GRAVITY = 0.35f;

    public static void onAnimateTick(BlockPos pos, BlockState state, RandomSource random) {
        Block block = state.getBlock();
        boolean campfire = block instanceof CampfireBlock;
        if (!campfire && !(block instanceof AbstractFurnaceBlock)) return;
        if (!ParticularConfig.embers()) return;

        Minecraft mc = Minecraft.getInstance();
        ParticleStatus status = mc.options.particles().get();
        if (status == ParticleStatus.MINIMAL || mc.player == null) return;
        if (mc.player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > RANGE_SQ) return;

        double amount = ParticularConfig.COMMON.emberAmount.get() * (status == ParticleStatus.DECREASED ? 0.5 : 1.0);

        if (campfire) {
            if (!ParticularConfig.COMMON.campfireEmbers.get() || !state.getValue(CampfireBlock.LIT)) return;

            boolean soul = block == Blocks.SOUL_CAMPFIRE;
            boolean signal = state.getValue(CampfireBlock.SIGNAL_FIRE);
            int count = count(random, (3 + random.nextInt(4)) * amount);
            for (int i = 0; i < count; i++) {
                EmberParticle ember = spawn(mc,
                        pos.getX() + 0.25 + random.nextDouble() * 0.5,
                        pos.getY() + 0.4 + random.nextDouble() * 0.2,
                        pos.getZ() + 0.25 + random.nextDouble() * 0.5,
                        random.nextDouble() * 0.02 - 0.01,
                        (0.03 + random.nextDouble() * 0.04) * (signal ? 1.6 : 1.0),
                        random.nextDouble() * 0.02 - 0.01);
                if (ember != null) {
                    ember.configure(soul, random.nextInt(CAMPFIRE_STAGGER), 0f, (30 + random.nextInt(30)) * (signal ? 2 : 1));
                }
            }
            return;
        }

        if (!ParticularConfig.COMMON.furnaceEmbers.get() || !state.getValue(AbstractFurnaceBlock.LIT)) return;

        Direction facing = state.getValue(AbstractFurnaceBlock.FACING);
        Direction side = facing.getClockWise();
        int count = count(random, (1 + random.nextInt(3)) * amount);
        for (int i = 0; i < count; i++) {
            double across = random.nextDouble() * 0.6 - 0.3;
            double push = 0.02 + random.nextDouble() * 0.04;
            EmberParticle ember = spawn(mc,
                    pos.getX() + 0.5 + facing.getStepX() * 0.52 + side.getStepX() * across,
                    pos.getY() + 0.1 + random.nextDouble() * 0.35,
                    pos.getZ() + 0.5 + facing.getStepZ() * 0.52 + side.getStepZ() * across,
                    facing.getStepX() * push + random.nextDouble() * 0.01 - 0.005,
                    0.03 + random.nextDouble() * 0.05,
                    facing.getStepZ() * push + random.nextDouble() * 0.01 - 0.005);
            if (ember != null) {
                ember.configure(false, random.nextInt(FURNACE_STAGGER), FURNACE_GRAVITY, 16 + random.nextInt(16));
            }
        }
    }

    private static int count(RandomSource random, double amount) {
        int whole = (int) amount;
        return whole + (random.nextDouble() < amount - whole ? 1 : 0);
    }

    private static EmberParticle spawn(Minecraft mc, double x, double y, double z, double velX, double velY, double velZ) {
        Particle particle = mc.particleEngine.createParticle(Particles.EMBER(), x, y, z, velX, velY, velZ);
        return particle instanceof EmberParticle ember ? ember : null;
    }
}
