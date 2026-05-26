package com.leclowndu93150.particular.mixin.sable;

import com.leclowndu93150.particular.compat.sable.SableCompat;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.ryanhcode.sable.sublevel.plot.LevelPlot", remap = false)
public abstract class SableLevelPlotMixin {
    @Inject(method = "onBlockChange", at = @At("TAIL"), require = 0, remap = false)
    private void particular$invalidateShape(BlockPos pos, BlockState state, CallbackInfo ci) {
        SableCompat.invalidateShapeFromPlot(this);
    }

    @Inject(method = "setBoundingBox", at = @At("TAIL"), require = 0, remap = false)
    private void particular$invalidateShapeOnBoundsSync(BoundingBox3ic bounds, CallbackInfo ci) {
        SableCompat.invalidateShapeFromPlot(this);
    }
}
