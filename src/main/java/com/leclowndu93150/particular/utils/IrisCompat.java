package com.leclowndu93150.particular.utils;

import net.irisshaders.iris.api.v0.IrisApi;
import net.neoforged.fml.ModList;

public class IrisCompat {

    public static boolean areShadersEnabled() {
        if (ModList.get().isLoaded("iris")) {
            return IrisApi.getInstance().getConfig().areShadersEnabled();
        }
        return false;
    }

}