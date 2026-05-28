package com.leclowndu93150.particular.mixin.sable;

import com.leclowndu93150.particular.compat.sable.SableCompat;
import dev.ryanhcode.sable.sublevel.SubLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.ryanhcode.sable.sublevel.ClientSubLevel", remap = false)
public abstract class SubLevelTickMixin {
    @Inject(method = "tick", at = @At("TAIL"), require = 0, remap = false)
    private void particular$tickWake(CallbackInfo ci) {
        SableCompat.tickSubLevelWake((SubLevel) (Object) this);
    }
}
