package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.platform.Services;
import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat {

    public static boolean areShadersEnabled() {
        if (Services.PLATFORM.isModLoaded(("iris"))) {
            return IrisApi.getInstance().getConfig().areShadersEnabled();
        }
        return false;
    }

}