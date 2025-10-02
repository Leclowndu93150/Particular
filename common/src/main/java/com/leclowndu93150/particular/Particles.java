package com.leclowndu93150.particular;

import com.leclowndu93150.particular.platform.Services;
import net.minecraft.core.particles.SimpleParticleType;

public class Particles {
    private static SimpleParticleType oakLeaf;
    private static SimpleParticleType birchLeaf;
    private static SimpleParticleType spruceLeaf;
    private static SimpleParticleType jungleLeaf;
    private static SimpleParticleType acaciaLeaf;
    private static SimpleParticleType darkOakLeaf;
    private static SimpleParticleType azaleaLeaf;
    private static SimpleParticleType mangroveLeaf;
    private static SimpleParticleType waterRipple;
    private static SimpleParticleType enderBubble;
    private static SimpleParticleType enderBubblePop;
    private static SimpleParticleType caveDust;
    private static SimpleParticleType firefly;
    private static SimpleParticleType waterfallSpray;
    private static SimpleParticleType cascade;
    private static SimpleParticleType waterSplashEmitter;
    private static SimpleParticleType waterSplash;
    private static SimpleParticleType waterSplashFoam;
    private static SimpleParticleType waterSplashRing;
    
    public static SimpleParticleType OAK_LEAF() { if (oakLeaf == null) oakLeaf = Services.PARTICLES.getParticle("oak_leaf"); return oakLeaf; }
    public static SimpleParticleType BIRCH_LEAF() { if (birchLeaf == null) birchLeaf = Services.PARTICLES.getParticle("birch_leaf"); return birchLeaf; }
    public static SimpleParticleType SPRUCE_LEAF() { if (spruceLeaf == null) spruceLeaf = Services.PARTICLES.getParticle("spruce_leaf"); return spruceLeaf; }
    public static SimpleParticleType JUNGLE_LEAF() { if (jungleLeaf == null) jungleLeaf = Services.PARTICLES.getParticle("jungle_leaf"); return jungleLeaf; }
    public static SimpleParticleType ACACIA_LEAF() { if (acaciaLeaf == null) acaciaLeaf = Services.PARTICLES.getParticle("acacia_leaf"); return acaciaLeaf; }
    public static SimpleParticleType DARK_OAK_LEAF() { if (darkOakLeaf == null) darkOakLeaf = Services.PARTICLES.getParticle("dark_oak_leaf"); return darkOakLeaf; }
    public static SimpleParticleType AZALEA_LEAF() { if (azaleaLeaf == null) azaleaLeaf = Services.PARTICLES.getParticle("azalea_leaf"); return azaleaLeaf; }
    public static SimpleParticleType MANGROVE_LEAF() { if (mangroveLeaf == null) mangroveLeaf = Services.PARTICLES.getParticle("mangrove_leaf"); return mangroveLeaf; }
    public static SimpleParticleType WATER_RIPPLE() { if (waterRipple == null) waterRipple = Services.PARTICLES.getParticle("water_ripple"); return waterRipple; }
    public static SimpleParticleType ENDER_BUBBLE() { if (enderBubble == null) enderBubble = Services.PARTICLES.getParticle("ender_bubble"); return enderBubble; }
    public static SimpleParticleType ENDER_BUBBLE_POP() { if (enderBubblePop == null) enderBubblePop = Services.PARTICLES.getParticle("ender_bubble_pop"); return enderBubblePop; }
    public static SimpleParticleType CAVE_DUST() { if (caveDust == null) caveDust = Services.PARTICLES.getParticle("cave_dust"); return caveDust; }
    public static SimpleParticleType FIREFLY() { if (firefly == null) firefly = Services.PARTICLES.getParticle("firefly"); return firefly; }
    public static SimpleParticleType WATERFALL_SPRAY() { if (waterfallSpray == null) waterfallSpray = Services.PARTICLES.getParticle("waterfall_spray"); return waterfallSpray; }
    public static SimpleParticleType CASCADE() { if (cascade == null) cascade = Services.PARTICLES.getParticle("cascade"); return cascade; }
    public static SimpleParticleType WATER_SPLASH_EMITTER() { if (waterSplashEmitter == null) waterSplashEmitter = Services.PARTICLES.getParticle("water_splash_emitter"); return waterSplashEmitter; }
    public static SimpleParticleType WATER_SPLASH() { if (waterSplash == null) waterSplash = Services.PARTICLES.getParticle("water_splash"); return waterSplash; }
    public static SimpleParticleType WATER_SPLASH_FOAM() { if (waterSplashFoam == null) waterSplashFoam = Services.PARTICLES.getParticle("water_splash_foam"); return waterSplashFoam; }
    public static SimpleParticleType WATER_SPLASH_RING() { if (waterSplashRing == null) waterSplashRing = Services.PARTICLES.getParticle("water_splash_ring"); return waterSplashRing; }

    public static void init() {
        Services.PARTICLES.registerParticle("oak_leaf", false);
        Services.PARTICLES.registerParticle("birch_leaf", false);
        Services.PARTICLES.registerParticle("spruce_leaf", false);
        Services.PARTICLES.registerParticle("jungle_leaf", false);
        Services.PARTICLES.registerParticle("acacia_leaf", false);
        Services.PARTICLES.registerParticle("dark_oak_leaf", false);
        Services.PARTICLES.registerParticle("azalea_leaf", false);
        Services.PARTICLES.registerParticle("mangrove_leaf", false);
        Services.PARTICLES.registerParticle("water_ripple", false);
        Services.PARTICLES.registerParticle("ender_bubble", false);
        Services.PARTICLES.registerParticle("ender_bubble_pop", false);
        Services.PARTICLES.registerParticle("cave_dust", false);
        Services.PARTICLES.registerParticle("firefly", false);
        Services.PARTICLES.registerParticle("waterfall_spray", false);
        Services.PARTICLES.registerParticle("cascade", true);
        Services.PARTICLES.registerParticle("water_splash_emitter", true);
        Services.PARTICLES.registerParticle("water_splash", true);
        Services.PARTICLES.registerParticle("water_splash_foam", true);
        Services.PARTICLES.registerParticle("water_splash_ring", true);
    }
}
