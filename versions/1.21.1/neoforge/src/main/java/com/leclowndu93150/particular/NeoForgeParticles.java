package com.leclowndu93150.particular;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Constants.MOD_ID);

    public static final Supplier<SimpleParticleType> OAK_LEAF = PARTICLE_TYPES.register("oak_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BIRCH_LEAF = PARTICLE_TYPES.register("birch_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SPRUCE_LEAF = PARTICLE_TYPES.register("spruce_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> JUNGLE_LEAF = PARTICLE_TYPES.register("jungle_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ACACIA_LEAF = PARTICLE_TYPES.register("acacia_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> DARK_OAK_LEAF = PARTICLE_TYPES.register("dark_oak_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> AZALEA_LEAF = PARTICLE_TYPES.register("azalea_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> MANGROVE_LEAF = PARTICLE_TYPES.register("mangrove_leaf", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> WHITE_OAK_LEAF = PARTICLE_TYPES.register("white_oak_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WHITE_SPRUCE_LEAF = PARTICLE_TYPES.register("white_spruce_leaf", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> MAPLE_LEAF = PARTICLE_TYPES.register("maple_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BRIMWOOD_LEAF = PARTICLE_TYPES.register("brimwood_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RU_BAOBAB_LEAF = PARTICLE_TYPES.register("ru_baobab_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> KAPOK_LEAF = PARTICLE_TYPES.register("kapok_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> EUCALYPTUS_LEAF = PARTICLE_TYPES.register("eucalyptus_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> REDWOOD_LEAF = PARTICLE_TYPES.register("redwood_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> MAGNOLIA_LEAF = PARTICLE_TYPES.register("magnolia_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RU_PALM_LEAF = PARTICLE_TYPES.register("ru_palm_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> LARCH_LEAF = PARTICLE_TYPES.register("larch_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> GOLDEN_LARCH_LEAF = PARTICLE_TYPES.register("golden_larch_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SOCOTRA_LEAF = PARTICLE_TYPES.register("socotra_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BAMBOO_LEAF = PARTICLE_TYPES.register("bamboo_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WILLOW_LEAF = PARTICLE_TYPES.register("willow_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RU_CYPRESS_LEAF = PARTICLE_TYPES.register("ru_cypress_leaf", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> WW_BAOBAB_LEAF = PARTICLE_TYPES.register("ww_baobab_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WW_CYPRESS_LEAF = PARTICLE_TYPES.register("ww_cypress_leaf", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WW_PALM_LEAF = PARTICLE_TYPES.register("ww_palm_leaf", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> WATER_RIPPLE = PARTICLE_TYPES.register("water_ripple", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ENDER_BUBBLE = PARTICLE_TYPES.register("ender_bubble", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ENDER_BUBBLE_POP = PARTICLE_TYPES.register("ender_bubble_pop", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> CAVE_DUST = PARTICLE_TYPES.register("cave_dust", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WATERFALL_SPRAY = PARTICLE_TYPES.register("waterfall_spray", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> CASCADE = PARTICLE_TYPES.register("cascade", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> WATER_SPLASH_EMITTER = PARTICLE_TYPES.register("water_splash_emitter", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> WATER_SPLASH = PARTICLE_TYPES.register("water_splash", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> WATER_SPLASH_FOAM = PARTICLE_TYPES.register("water_splash_foam", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> WATER_SPLASH_RING = PARTICLE_TYPES.register("water_splash_ring", () -> new SimpleParticleType(true));

    public static DeferredRegister<ParticleType<?>> getRegistry() {
        return PARTICLE_TYPES;
    }
}
