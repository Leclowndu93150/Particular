package com.leclowndu93150.particular.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

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
        public static final Codec<Options> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Mode.CODEC.fieldOf("mode").forGetter(Options::mode),
                Codec.INT.fieldOf("color").forGetter(Options::color),
                Codec.BOOL.fieldOf("biome_tint").forGetter(Options::biomeTint)
        ).apply(instance, Options::new));

        public static final Deserializer<Options> DESERIALIZER = new Deserializer<>() {
            @Override
            public Options fromCommand(ParticleType<Options> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int modeId = reader.readInt();
                reader.expect(' ');
                int color = reader.readInt();
                reader.expect(' ');
                boolean tint = reader.readBoolean();
                return new Options(Mode.byId(modeId), color, tint);
            }

            @Override
            public Options fromNetwork(ParticleType<Options> particleType, FriendlyByteBuf buf) {
                return new Options(Mode.byId(buf.readVarInt()), buf.readInt(), buf.readBoolean());
            }
        };

        @Override
        public ParticleType<Options> getType() {
            return com.leclowndu93150.particular.Particles.CUBOID();
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buf) {
            buf.writeVarInt(this.mode.id());
            buf.writeInt(this.color);
            buf.writeBoolean(this.biomeTint);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %d %d %s",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.mode.id(),
                    this.color,
                    this.biomeTint);
        }
    }

    public CuboidParticleType(boolean overrideLimiter) {
        super(overrideLimiter, Options.DESERIALIZER);
    }

    @Override
    public Codec<Options> codec() {
        return Options.CODEC;
    }
}
