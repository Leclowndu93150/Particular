package com.leclowndu93150.particular.mixin.honey;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin extends HalfTransparentBlock {


    public HoneyBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.isClientSide && ParticularConfig.honeyDrips()) {
            int particleCount = random.nextInt(1) + 1;
            
            for (int i = 0; i < particleCount; ++i) {
                this.trySpawnDripParticles(level, pos, state);
            }
        }
    }

    private void trySpawnDripParticles(Level level, BlockPos pos, BlockState state) {

        boolean fluidEmpty = state.getFluidState().isEmpty();
        float randomValue = level.random.nextFloat();
        boolean passesRandomCheck = !(randomValue < 0.3F);

        if (fluidEmpty && passesRandomCheck) {
            VoxelShape voxelshape = state.getCollisionShape(level, pos);
            double d0 = voxelshape.max(Direction.Axis.Y);
            boolean impermeableCheck = !state.is(BlockTags.IMPERMEABLE);

            if (d0 >= (double)0.9F && impermeableCheck) {
                double d1 = voxelshape.min(Direction.Axis.Y);

                if (d1 > (double)0.0F) {
                    this.spawnParticle(level, pos, voxelshape, (double)pos.getY() + d1 - 0.05);
                } else {
                    BlockPos blockpos = pos.below();
                    BlockState blockstate = level.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getCollisionShape(level, blockpos);
                    double d2 = voxelshape1.max(Direction.Axis.Y);
                    boolean belowCheck = (d2 < (double)1.0F || !blockstate.isCollisionShapeFullBlock(level, blockpos)) && blockstate.getFluidState().isEmpty();

                    if (belowCheck) {
                        this.spawnParticle(level, pos, voxelshape, (double)pos.getY() - 0.05);
                    }
                }
            }
        }
    }

    private void spawnParticle(Level level, BlockPos pos, VoxelShape shape, double y) {
        double minX = (double)pos.getX() + shape.min(Direction.Axis.X);
        double maxX = (double)pos.getX() + shape.max(Direction.Axis.X);
        double minZ = (double)pos.getZ() + shape.min(Direction.Axis.Z);
        double maxZ = (double)pos.getZ() + shape.max(Direction.Axis.Z);
        this.spawnFluidParticle(level, minX, maxX, minZ, maxZ, y);
    }

    private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y) {
        double finalX = Mth.lerp(level.random.nextDouble(), x1, x2);
        double finalZ = Mth.lerp(level.random.nextDouble(), z1, z2);
        level.addParticle(ParticleTypes.DRIPPING_HONEY, finalX, y, finalZ, (double)0.0F, (double)0.0F, (double)0.0F);
    }
}
