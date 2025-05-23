package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpongeBlock.class)
public class InjectSpongeBlock {

    @Inject(
            method = "tryAbsorbWater",
            at = @At("RETURN")
    )
    private void onSpongeAbsorbWater(Level level, BlockPos pos, CallbackInfo ci) {
        if (!ParticularConfig.cascades() || !level.isClientSide()) return;

        for (int dx = -7; dx <= 7; dx++) {
            for (int dy = -7; dy <= 7; dy++) {
                for (int dz = -7; dz <= 7; dz++) {
                    BlockPos checkPos = pos.offset(dx, dy, dz);
                    FluidState fluidState = level.getFluidState(checkPos);
                    Main.updateCascade(level, checkPos, fluidState);
                }
            }
        }
    }
}
