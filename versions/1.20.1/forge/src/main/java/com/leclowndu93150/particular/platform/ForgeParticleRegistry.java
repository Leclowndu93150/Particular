package com.leclowndu93150.particular.platform;

import com.leclowndu93150.particular.ForgeParticles;
import com.leclowndu93150.particular.platform.services.IParticleRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ForgeParticleRegistry implements IParticleRegistry {
    private static final Map<String, RegistryObject<SimpleParticleType>> PARTICLES = new HashMap<>();

    static {
        PARTICLES.put("oak_leaf", ForgeParticles.OAK_LEAF);
        PARTICLES.put("birch_leaf", ForgeParticles.BIRCH_LEAF);
        PARTICLES.put("spruce_leaf", ForgeParticles.SPRUCE_LEAF);
        PARTICLES.put("jungle_leaf", ForgeParticles.JUNGLE_LEAF);
        PARTICLES.put("acacia_leaf", ForgeParticles.ACACIA_LEAF);
        PARTICLES.put("dark_oak_leaf", ForgeParticles.DARK_OAK_LEAF);
        PARTICLES.put("azalea_leaf", ForgeParticles.AZALEA_LEAF);
        PARTICLES.put("mangrove_leaf", ForgeParticles.MANGROVE_LEAF);
        PARTICLES.put("white_oak_leaf", ForgeParticles.WHITE_OAK_LEAF);
        PARTICLES.put("white_spruce_leaf", ForgeParticles.WHITE_SPRUCE_LEAF);
        PARTICLES.put("maple_leaf", ForgeParticles.MAPLE_LEAF);
        PARTICLES.put("brimwood_leaf", ForgeParticles.BRIMWOOD_LEAF);
        PARTICLES.put("ru_baobab_leaf", ForgeParticles.RU_BAOBAB_LEAF);
        PARTICLES.put("kapok_leaf", ForgeParticles.KAPOK_LEAF);
        PARTICLES.put("eucalyptus_leaf", ForgeParticles.EUCALYPTUS_LEAF);
        PARTICLES.put("redwood_leaf", ForgeParticles.REDWOOD_LEAF);
        PARTICLES.put("magnolia_leaf", ForgeParticles.MAGNOLIA_LEAF);
        PARTICLES.put("ru_palm_leaf", ForgeParticles.RU_PALM_LEAF);
        PARTICLES.put("larch_leaf", ForgeParticles.LARCH_LEAF);
        PARTICLES.put("golden_larch_leaf", ForgeParticles.GOLDEN_LARCH_LEAF);
        PARTICLES.put("socotra_leaf", ForgeParticles.SOCOTRA_LEAF);
        PARTICLES.put("bamboo_leaf", ForgeParticles.BAMBOO_LEAF);
        PARTICLES.put("willow_leaf", ForgeParticles.WILLOW_LEAF);
        PARTICLES.put("ru_cypress_leaf", ForgeParticles.RU_CYPRESS_LEAF);
        PARTICLES.put("ww_baobab_leaf", ForgeParticles.WW_BAOBAB_LEAF);
        PARTICLES.put("ww_cypress_leaf", ForgeParticles.WW_CYPRESS_LEAF);
        PARTICLES.put("ww_palm_leaf", ForgeParticles.WW_PALM_LEAF);
        PARTICLES.put("water_ripple", ForgeParticles.WATER_RIPPLE);
        PARTICLES.put("ender_bubble", ForgeParticles.ENDER_BUBBLE);
        PARTICLES.put("ender_bubble_pop", ForgeParticles.ENDER_BUBBLE_POP);
        PARTICLES.put("cave_dust", ForgeParticles.CAVE_DUST);
        PARTICLES.put("firefly", ForgeParticles.FIREFLY);
        PARTICLES.put("waterfall_spray", ForgeParticles.WATERFALL_SPRAY);
        PARTICLES.put("cascade", ForgeParticles.CASCADE);
        PARTICLES.put("water_splash_emitter", ForgeParticles.WATER_SPLASH_EMITTER);
        PARTICLES.put("water_splash", ForgeParticles.WATER_SPLASH);
        PARTICLES.put("water_splash_foam", ForgeParticles.WATER_SPLASH_FOAM);
        PARTICLES.put("water_splash_ring", ForgeParticles.WATER_SPLASH_RING);
    }

    @Override
    public SimpleParticleType registerParticle(String name, boolean alwaysShow) {
        return null;
    }

    @Override
    public SimpleParticleType getParticle(String name) {
        RegistryObject<SimpleParticleType> obj = PARTICLES.get(name);
        return obj != null ? obj.get() : null;
    }
}
