package com.leclowndu93150.particular;

import com.leclowndu93150.particular.particles.*;
import com.leclowndu93150.particular.particles.leaves.BigLeafParticle;
import com.leclowndu93150.particular.particles.leaves.ConiferLeafParticle;
import com.leclowndu93150.particular.particles.leaves.LeafParticle;
import com.leclowndu93150.particular.particles.leaves.SpinningLeafParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashEmitterParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashFoamParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashParticle;
import com.leclowndu93150.particular.particles.splashes.WaterSplashRingParticle;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Particles {
	private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Main.MOD_ID);

	// Vanilla leaves
	public static final Supplier<SimpleParticleType> OAK_LEAF = registerParticle("oak_leaf");
	public static final Supplier<SimpleParticleType> BIRCH_LEAF = registerParticle("birch_leaf");
	public static final Supplier<SimpleParticleType> SPRUCE_LEAF = registerParticle("spruce_leaf");
	public static final Supplier<SimpleParticleType> JUNGLE_LEAF = registerParticle("jungle_leaf");
	public static final Supplier<SimpleParticleType> ACACIA_LEAF = registerParticle("acacia_leaf");
	public static final Supplier<SimpleParticleType> DARK_OAK_LEAF = registerParticle("dark_oak_leaf");
	public static final Supplier<SimpleParticleType> AZALEA_LEAF = registerParticle("azalea_leaf");
	public static final Supplier<SimpleParticleType> MANGROVE_LEAF = registerParticle("mangrove_leaf");

	// Generic leaves
	public static final Supplier<SimpleParticleType> WHITE_OAK_LEAF = registerParticle("white_oak_leaf");
	public static final Supplier<SimpleParticleType> WHITE_SPRUCE_LEAF = registerParticle("white_spruce_leaf");

	// Regions Unexplored leaves
	public static final Supplier<SimpleParticleType> MAPLE_LEAF = registerParticle("maple_leaf");
	public static final Supplier<SimpleParticleType> BRIMWOOD_LEAF = registerParticle("brimwood_leaf");
	public static final Supplier<SimpleParticleType> RU_BAOBAB_LEAF = registerParticle("ru_baobab_leaf");
	public static final Supplier<SimpleParticleType> KAPOK_LEAF = registerParticle("kapok_leaf");
	public static final Supplier<SimpleParticleType> EUCALYPTUS_LEAF = registerParticle("eucalyptus_leaf");
	public static final Supplier<SimpleParticleType> REDWOOD_LEAF = registerParticle("redwood_leaf");
	public static final Supplier<SimpleParticleType> MAGNOLIA_LEAF = registerParticle("magnolia_leaf");
	public static final Supplier<SimpleParticleType> RU_PALM_LEAF = registerParticle("ru_palm_leaf");
	public static final Supplier<SimpleParticleType> LARCH_LEAF = registerParticle("larch_leaf");
	public static final Supplier<SimpleParticleType> GOLDEN_LARCH_LEAF = registerParticle("golden_larch_leaf");
	public static final Supplier<SimpleParticleType> SOCOTRA_LEAF = registerParticle("socotra_leaf");
	public static final Supplier<SimpleParticleType> BAMBOO_LEAF = registerParticle("bamboo_leaf");
	public static final Supplier<SimpleParticleType> WILLOW_LEAF = registerParticle("willow_leaf");
	public static final Supplier<SimpleParticleType> RU_CYPRESS_LEAF = registerParticle("ru_cypress_leaf");

	// Wilder World leaves
	public static final Supplier<SimpleParticleType> WW_BAOBAB_LEAF = registerParticle("ww_baobab_leaf");
	public static final Supplier<SimpleParticleType> WW_CYPRESS_LEAF = registerParticle("ww_cypress_leaf");
	public static final Supplier<SimpleParticleType> WW_PALM_LEAF = registerParticle("ww_palm_leaf");

	// Other particles
	public static final Supplier<SimpleParticleType> WATER_RIPPLE = registerParticle("water_ripple");
	public static final Supplier<SimpleParticleType> ENDER_BUBBLE = registerParticle("ender_bubble");
	public static final Supplier<SimpleParticleType> ENDER_BUBBLE_POP = registerParticle("ender_bubble_pop");
	public static final Supplier<SimpleParticleType> CAVE_DUST = registerParticle("cave_dust");
	public static final Supplier<SimpleParticleType> FIREFLY = registerParticle("firefly");
	public static final Supplier<SimpleParticleType> WATERFALL_SPRAY = registerParticle("waterfall_spray");
	public static final Supplier<SimpleParticleType> CASCADE = registerParticle("cascade", true);

	// Water splash particles
	public static final Supplier<SimpleParticleType> WATER_SPLASH_EMITTER = registerParticle("water_splash_emitter", true);
	public static final Supplier<SimpleParticleType> WATER_SPLASH = registerParticle("water_splash", true);
	public static final Supplier<SimpleParticleType> WATER_SPLASH_FOAM = registerParticle("water_splash_foam", true);
	public static final Supplier<SimpleParticleType> WATER_SPLASH_RING = registerParticle("water_splash_ring", true);

	public static void register(IEventBus modEventBus) {
		PARTICLE_TYPES.register(modEventBus);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(OAK_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(BIRCH_LEAF.get(), SpinningLeafParticle.Factory::new);
		event.registerSpriteSet(SPRUCE_LEAF.get(), ConiferLeafParticle.Factory::new);
		event.registerSpriteSet(JUNGLE_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(ACACIA_LEAF.get(), SpinningLeafParticle.Factory::new);
		event.registerSpriteSet(DARK_OAK_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(AZALEA_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(MANGROVE_LEAF.get(), LeafParticle.Factory::new);

		event.registerSpriteSet(WHITE_OAK_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(WHITE_SPRUCE_LEAF.get(), ConiferLeafParticle.Factory::new);

		event.registerSpriteSet(MAPLE_LEAF.get(), SpinningLeafParticle.Factory::new);
		event.registerSpriteSet(BRIMWOOD_LEAF.get(), SpinningLeafParticle.Factory::new);
		event.registerSpriteSet(RU_BAOBAB_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(KAPOK_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(EUCALYPTUS_LEAF.get(), BigLeafParticle.Factory::new);
		event.registerSpriteSet(REDWOOD_LEAF.get(), ConiferLeafParticle.Factory::new);
		event.registerSpriteSet(MAGNOLIA_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(RU_PALM_LEAF.get(), BigLeafParticle.Factory::new);
		event.registerSpriteSet(LARCH_LEAF.get(), ConiferLeafParticle.Factory::new);
		event.registerSpriteSet(GOLDEN_LARCH_LEAF.get(), ConiferLeafParticle.Factory::new);
		event.registerSpriteSet(SOCOTRA_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(BAMBOO_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(WILLOW_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(RU_CYPRESS_LEAF.get(), ConiferLeafParticle.Factory::new);

		event.registerSpriteSet(WW_BAOBAB_LEAF.get(), SpinningLeafParticle.Factory::new);
		event.registerSpriteSet(WW_CYPRESS_LEAF.get(), ConiferLeafParticle.Factory::new);
		event.registerSpriteSet(WW_PALM_LEAF.get(), BigLeafParticle.Factory::new);

		event.registerSpriteSet(WATER_RIPPLE.get(), WaterRippleParticle.Factory::new);
		event.registerSpriteSet(ENDER_BUBBLE.get(), EnderBubbleParticle.Factory::new);
		event.registerSpriteSet(ENDER_BUBBLE_POP.get(), BubblePopParticle.Provider::new);
		event.registerSpriteSet(CAVE_DUST.get(), CaveDustParticle.Factory::new);
		event.registerSpriteSet(FIREFLY.get(), FireflyParticle.Factory::new);
		event.registerSpriteSet(WATERFALL_SPRAY.get(), WaterfallSprayParticle.Factory::new);
		event.registerSpriteSet(CASCADE.get(), CascadeParticle.Factory::new);

		event.registerSpriteSet(WATER_SPLASH_EMITTER.get(), WaterSplashEmitterParticle.Factory::new);
		event.registerSpriteSet(WATER_SPLASH.get(), WaterSplashParticle.Factory::new);
		event.registerSpriteSet(WATER_SPLASH_FOAM.get(), WaterSplashFoamParticle.Factory::new);
		event.registerSpriteSet(WATER_SPLASH_RING.get(), WaterSplashRingParticle.Factory::new);
	}

	private static Supplier<SimpleParticleType> registerParticle(String name) {
		return registerParticle(name, false);
	}

	private static Supplier<SimpleParticleType> registerParticle(String name, boolean alwaysShow) {
		return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
	}
}