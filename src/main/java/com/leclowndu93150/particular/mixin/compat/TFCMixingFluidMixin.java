package com.leclowndu93150.particular.mixin.compat;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MixingFluid.class)
public abstract class TFCMixingFluidMixin extends ForgeFlowingFluid {
    private static final Logger LOGGER = LogManager.getLogger("Particular-TFC");

    protected TFCMixingFluidMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        super.animateTick(level, pos, state, random);

        if (!level.isClientSide || !ParticularConfig.waterfallSpray()) {
            return;
        }

        if (!state.is(TFCTags.Fluids.ANY_WATER)) {
            return;
        }

        if (!state.isSource() && level.getFluidState(pos.below()).is(TFCTags.Fluids.ANY_WATER)) {
            // Splishy splashy
            for (int i = 0; i < 2; ++i) {
                if (state.getValue(BlockStateProperties.FALLING)) {
                    double x = pos.getX();
                    double y = (double) pos.getY() + random.nextDouble();
                    double z = pos.getZ();
                    if (random.nextBoolean()) {
                        x += random.nextDouble();
                        z += random.nextIntBetweenInclusive(0, 1);
                    } else {
                        x += random.nextIntBetweenInclusive(0, 1);
                        z += random.nextDouble();
                    }
                    level.addParticle(Particles.WATERFALL_SPRAY.get(), x, y, z, 0.0, 0.0, 0.0);
                    System.out.println("TRYING TO SPAWN CASCADE");
                } else {
                    double x = (double) pos.getX() + random.nextDouble();
                    double y = (double) pos.getY() + (random.nextDouble() * state.getOwnHeight());
                    double z = (double) pos.getZ() + random.nextDouble();
                    Vec3 vel = state.getFlow(level, pos).scale(0.075);
                    level.addParticle(Particles.WATERFALL_SPRAY.get(), x, y, z, vel.x, 0.0, vel.z);
                    System.out.println("TRYING TO SPAWN CASCADE");
                }
            }
        }
    }
}