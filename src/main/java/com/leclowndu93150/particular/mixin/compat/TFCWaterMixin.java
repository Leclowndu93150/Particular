package com.leclowndu93150.particular.mixin.compat;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.fluids.RiverWaterFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({RiverWaterFluid.class})
public abstract class TFCWaterMixin extends WaterFluid {

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !(Boolean)state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                level.playLocalSound((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), (double)0.0F, (double)0.0F, (double)0.0F);
        }

        if (!ParticularConfig.waterfallSpray()) { return; }

        if (!state.isSource() &&
                level.getFluidState(pos.below()).is(TFCTags.Fluids.ANY_WATER))
        {
            // Splishy splashy
            for (int i = 0; i < 2; ++i)
            {
                if (state.getValue(BlockStateProperties.FALLING))
                {
                    double x = pos.getX();
                    double y = (double) pos.getY() + random.nextDouble();
                    double z = pos.getZ();

                    if (random.nextBoolean())
                    {
                        x += random.nextDouble();
                        z += random.nextIntBetweenInclusive(0, 1);
                    }
                    else
                    {
                        x += random.nextIntBetweenInclusive(0, 1);
                        z += random.nextDouble();
                    }

                    level.addParticle(Particles.WATERFALL_SPRAY.get(), x, y, z, 0.0, 0.0, 0.0);
                }
                else
                {
                    double x = (double) pos.getX() + random.nextDouble();
                    double y = (double) pos.getY() + (random.nextDouble() * state.getOwnHeight());
                    double z = (double) pos.getZ() + random.nextDouble();
                    Vec3 vel = state.getFlow(level, pos).scale(0.075);
                    level.addParticle(Particles.WATERFALL_SPRAY.get(), x, y, z, vel.x, 0.0, vel.z);
                }
            }
        }

    }
}
