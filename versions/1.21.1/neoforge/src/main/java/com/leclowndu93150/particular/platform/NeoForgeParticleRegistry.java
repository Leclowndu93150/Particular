package com.leclowndu93150.particular.platform;

import com.leclowndu93150.particular.NeoForgeParticles;
import com.leclowndu93150.particular.platform.services.IParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeParticleRegistry implements IParticleRegistry {
    private static final Map<String, Supplier<? extends ParticleType<?>>> PARTICLES = new HashMap<>();

    static {
        PARTICLES.put("oak_leaf", NeoForgeParticles.OAK_LEAF);
        PARTICLES.put("birch_leaf", NeoForgeParticles.BIRCH_LEAF);
        PARTICLES.put("spruce_leaf", NeoForgeParticles.SPRUCE_LEAF);
        PARTICLES.put("jungle_leaf", NeoForgeParticles.JUNGLE_LEAF);
        PARTICLES.put("acacia_leaf", NeoForgeParticles.ACACIA_LEAF);
        PARTICLES.put("dark_oak_leaf", NeoForgeParticles.DARK_OAK_LEAF);
        PARTICLES.put("azalea_leaf", NeoForgeParticles.AZALEA_LEAF);
        PARTICLES.put("mangrove_leaf", NeoForgeParticles.MANGROVE_LEAF);
        PARTICLES.put("white_oak_leaf", NeoForgeParticles.WHITE_OAK_LEAF);
        PARTICLES.put("white_spruce_leaf", NeoForgeParticles.WHITE_SPRUCE_LEAF);
        PARTICLES.put("maple_leaf", NeoForgeParticles.MAPLE_LEAF);
        PARTICLES.put("brimwood_leaf", NeoForgeParticles.BRIMWOOD_LEAF);
        PARTICLES.put("ru_baobab_leaf", NeoForgeParticles.RU_BAOBAB_LEAF);
        PARTICLES.put("kapok_leaf", NeoForgeParticles.KAPOK_LEAF);
        PARTICLES.put("eucalyptus_leaf", NeoForgeParticles.EUCALYPTUS_LEAF);
        PARTICLES.put("redwood_leaf", NeoForgeParticles.REDWOOD_LEAF);
        PARTICLES.put("magnolia_leaf", NeoForgeParticles.MAGNOLIA_LEAF);
        PARTICLES.put("ru_palm_leaf", NeoForgeParticles.RU_PALM_LEAF);
        PARTICLES.put("larch_leaf", NeoForgeParticles.LARCH_LEAF);
        PARTICLES.put("golden_larch_leaf", NeoForgeParticles.GOLDEN_LARCH_LEAF);
        PARTICLES.put("socotra_leaf", NeoForgeParticles.SOCOTRA_LEAF);
        PARTICLES.put("bamboo_leaf", NeoForgeParticles.BAMBOO_LEAF);
        PARTICLES.put("willow_leaf", NeoForgeParticles.WILLOW_LEAF);
        PARTICLES.put("ru_cypress_leaf", NeoForgeParticles.RU_CYPRESS_LEAF);
        PARTICLES.put("ww_baobab_leaf", NeoForgeParticles.WW_BAOBAB_LEAF);
        PARTICLES.put("ww_cypress_leaf", NeoForgeParticles.WW_CYPRESS_LEAF);
        PARTICLES.put("ww_palm_leaf", NeoForgeParticles.WW_PALM_LEAF);
        PARTICLES.put("water_ripple", NeoForgeParticles.WATER_RIPPLE);
        PARTICLES.put("ender_bubble", NeoForgeParticles.ENDER_BUBBLE);
        PARTICLES.put("ender_bubble_pop", NeoForgeParticles.ENDER_BUBBLE_POP);
        PARTICLES.put("cave_dust", NeoForgeParticles.CAVE_DUST);
        PARTICLES.put("firefly", NeoForgeParticles.FIREFLY);
        PARTICLES.put("waterfall_spray", NeoForgeParticles.WATERFALL_SPRAY);
        PARTICLES.put("cascade", NeoForgeParticles.CASCADE);
        PARTICLES.put("water_splash_emitter", NeoForgeParticles.WATER_SPLASH_EMITTER);
        PARTICLES.put("water_splash", NeoForgeParticles.WATER_SPLASH);
        PARTICLES.put("water_splash_foam", NeoForgeParticles.WATER_SPLASH_FOAM);
        PARTICLES.put("water_splash_ring", NeoForgeParticles.WATER_SPLASH_RING);
        PARTICLES.put("cuboid", NeoForgeParticles.CUBOID);
    }

    @Override
    public <T extends ParticleOptions> ParticleType<T> register(String name, Supplier<? extends ParticleType<T>> factory) {
        return get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ParticleType<?>> T get(String name) {
        Supplier<? extends ParticleType<?>> supplier = PARTICLES.get(name);
        return supplier != null ? (T) supplier.get() : null;
    }

    @Override
    public SimpleParticleType registerParticle(String name, boolean alwaysShow) {
        return get(name);
    }
}
