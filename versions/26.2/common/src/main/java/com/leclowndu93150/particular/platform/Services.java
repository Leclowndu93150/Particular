package com.leclowndu93150.particular.platform;

import com.leclowndu93150.particular.Constants;
import com.leclowndu93150.particular.platform.services.IPlatformHelper;
import com.leclowndu93150.baguettelib.platform.services.IParticleRegistry;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IParticleRegistry PARTICLES = load(IParticleRegistry.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
