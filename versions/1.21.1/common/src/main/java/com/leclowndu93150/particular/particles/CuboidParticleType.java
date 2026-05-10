package com.leclowndu93150.particular.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class CuboidParticleType extends ParticleType<CuboidParticleType.Options> {
    public enum Mode {
        WATER(0),
        WATERFALL_SPRAY(1),
        SPLASH_WATER(2),
        LAVA(3);

        public static final Codec<Mode> CODEC = Codec.INT.xmap(Mode::byId, Mode::id);
        private final int id;

        Mode(int id) {
            this.id = id;
        }

        public int id() {
            return this.id;
        }

        public static Mode byId(int id) {
            for (Mode mode : values()) {
                if (mode.id == id) {
                    return mode;
                }
            }
            return WATER;
        }
    }

    public record Options(Mode mode, int color, boolean biomeTint) implements ParticleOptions {
        public static final MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Mode.CODEC.fieldOf("mode").forGetter(Options::mode),
                Codec.INT.fieldOf("color").forGetter(Options::color),
                Codec.BOOL.fieldOf("biome_tint").forGetter(Options::biomeTint)
        ).apply(instance, Options::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                options -> options.mode.id(),
                ByteBufCodecs.INT,
                Options::color,
                ByteBufCodecs.BOOL,
                Options::biomeTint,
                (mode, color, biomeTint) -> new Options(Mode.byId(mode), color, biomeTint)
        );

        @Override
        public ParticleType<Options> getType() {
            return com.leclowndu93150.particular.Particles.CUBOID();
        }
    }

    public CuboidParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<Options> codec() {
        return Options.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, Options> streamCodec() {
        return Options.STREAM_CODEC;
    }
}
