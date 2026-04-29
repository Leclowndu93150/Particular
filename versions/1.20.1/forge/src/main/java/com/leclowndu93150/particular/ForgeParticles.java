package com.leclowndu93150.particular;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ForgeParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Constants.MOD_ID);

    public static final RegistryObject<SimpleParticleType> OAK_LEAF = PARTICLE_TYPES.register("oak_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BIRCH_LEAF = PARTICLE_TYPES.register("birch_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SPRUCE_LEAF = PARTICLE_TYPES.register("spruce_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> JUNGLE_LEAF = PARTICLE_TYPES.register("jungle_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ACACIA_LEAF = PARTICLE_TYPES.register("acacia_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> DARK_OAK_LEAF = PARTICLE_TYPES.register("dark_oak_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> AZALEA_LEAF = PARTICLE_TYPES.register("azalea_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MANGROVE_LEAF = PARTICLE_TYPES.register("mangrove_leaf", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WHITE_OAK_LEAF = PARTICLE_TYPES.register("white_oak_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WHITE_SPRUCE_LEAF = PARTICLE_TYPES.register("white_spruce_leaf", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> MAPLE_LEAF = PARTICLE_TYPES.register("maple_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BRIMWOOD_LEAF = PARTICLE_TYPES.register("brimwood_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> RU_BAOBAB_LEAF = PARTICLE_TYPES.register("ru_baobab_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> KAPOK_LEAF = PARTICLE_TYPES.register("kapok_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> EUCALYPTUS_LEAF = PARTICLE_TYPES.register("eucalyptus_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> REDWOOD_LEAF = PARTICLE_TYPES.register("redwood_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MAGNOLIA_LEAF = PARTICLE_TYPES.register("magnolia_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> RU_PALM_LEAF = PARTICLE_TYPES.register("ru_palm_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> LARCH_LEAF = PARTICLE_TYPES.register("larch_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GOLDEN_LARCH_LEAF = PARTICLE_TYPES.register("golden_larch_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SOCOTRA_LEAF = PARTICLE_TYPES.register("socotra_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BAMBOO_LEAF = PARTICLE_TYPES.register("bamboo_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WILLOW_LEAF = PARTICLE_TYPES.register("willow_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> RU_CYPRESS_LEAF = PARTICLE_TYPES.register("ru_cypress_leaf", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WW_BAOBAB_LEAF = PARTICLE_TYPES.register("ww_baobab_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WW_CYPRESS_LEAF = PARTICLE_TYPES.register("ww_cypress_leaf", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WW_PALM_LEAF = PARTICLE_TYPES.register("ww_palm_leaf", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WATER_RIPPLE = PARTICLE_TYPES.register("water_ripple", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ENDER_BUBBLE = PARTICLE_TYPES.register("ender_bubble", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ENDER_BUBBLE_POP = PARTICLE_TYPES.register("ender_bubble_pop", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CAVE_DUST = PARTICLE_TYPES.register("cave_dust", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WATERFALL_SPRAY = PARTICLE_TYPES.register("waterfall_spray", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CASCADE = PARTICLE_TYPES.register("cascade", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> WATER_SPLASH_EMITTER = PARTICLE_TYPES.register("water_splash_emitter", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WATER_SPLASH = PARTICLE_TYPES.register("water_splash", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WATER_SPLASH_FOAM = PARTICLE_TYPES.register("water_splash_foam", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WATER_SPLASH_RING = PARTICLE_TYPES.register("water_splash_ring", () -> new SimpleParticleType(true));

    public static DeferredRegister<ParticleType<?>> getRegistry() {
        return PARTICLE_TYPES;
    }
}
