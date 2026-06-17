package com.leclowndu93150.particular;

import com.leclowndu93150.particular.particles.CuboidParticle;
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
    public static final Supplier<CuboidParticle.Type> CUBOID = PARTICLE_TYPES.register("cuboid", () -> new CuboidParticle.Type(true));

    public static DeferredRegister<ParticleType<?>> getRegistry() {
        return PARTICLE_TYPES;
    }
}
