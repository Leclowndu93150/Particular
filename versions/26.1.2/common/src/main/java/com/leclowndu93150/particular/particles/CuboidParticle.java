package com.leclowndu93150.particular.particles;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class CuboidParticle extends SingleQuadParticle {
    public enum Mode {
        WATER(0),
        WATERFALL_SPRAY(1),
        SPLASH_WATER(2),
        LAVA(3);

        private static final Codec<Mode> CODEC = Codec.INT.xmap(Mode::byId, Mode::id);
        private final int id;

        Mode(int id) {
            this.id = id;
        }

        private int id() {
            return this.id;
        }

        private static Mode byId(int id) {
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
            return Particles.CUBOID();
        }
    }

    public static class Type extends ParticleType<Options> {
        public Type(boolean overrideLimiter) {
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

    private static final float SIDE = 1.0F / 16.0F;
    private static final float HALF_SIDE = SIDE / 2.0F;
    private static final Quaternionf IDENTITY = new Quaternionf();
    private static final Quaternionf BACK = new Quaternionf().rotateY((float)Math.PI);
    private static final Quaternionf EAST = new Quaternionf().rotateY((float)(Math.PI * 0.5));
    private static final Quaternionf WEST = new Quaternionf().rotateY((float)(-Math.PI * 0.5));
    private static final Quaternionf UP = new Quaternionf().rotateX((float)(-Math.PI * 0.5));
    private static final Quaternionf DOWN = new Quaternionf().rotateX((float)(Math.PI * 0.5));

    private final Fluid fluid;
    private final Mode mode;

    private CuboidParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite, Options options) {
        super(level, x, y, z, sprite);
        this.mode = options.mode();
        this.fluid = this.mode == Mode.LAVA ? Fluids.LAVA : Fluids.WATER;
        this.gravity = 0.06F;
        this.friction = 0.98F;
        this.quadSize = HALF_SIDE;
        this.setSize(0.01F, 0.01F);
        if (this.mode == Mode.WATERFALL_SPRAY) {
            this.setWaterfallSprayMotion(xd, zd);
            this.lifetime = (int)(8.0 / (this.random.nextFloat() * 0.8 + 0.2));
        } else {
            this.xd = xd;
            this.yd = yd;
            this.zd = zd;
            this.lifetime = (int)(64.0 / (this.random.nextFloat() * 0.8 + 0.2));
        }
        int color = color(level, x, y, z, options);
        this.setColor(
                (color >> 16 & 0xFF) / 255.0F,
                (color >> 8 & 0xFF) / 255.0F,
                (color & 0xFF) / 255.0F
        );
    }

    private void setWaterfallSprayMotion(double xFlow, double zFlow) {
        double xVel = (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F;
        double yVel = (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F;
        double zVel = (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F;
        double speed = (this.random.nextFloat() + this.random.nextFloat() + 1.0F) * 0.15F;
        double length = Math.sqrt(xVel * xVel + yVel * yVel + zVel * zVel);
        this.xd = xVel / length * speed * 0.4F * 0.3F + xFlow;
        this.yd = (this.random.nextFloat() * 0.2F + 0.1F) * 0.75F;
        this.zd = zVel / length * speed * 0.4F * 0.3F + zFlow;
    }

    @Override
    public void tick() {
        if (this.mode == Mode.WATERFALL_SPRAY) {
            this.tickWaterfallSpray();
            return;
        }

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.afterMove();
        if (!this.removed) {
            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;

            BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
            FluidState fluidState = this.level.getFluidState(pos);
            if (fluidState.is(this.fluid) && this.y < pos.getY() + fluidState.getHeight(this.level, pos)) {
                if (this.mode != Mode.LAVA && ParticularConfig.waterDripRipples()) {
                    this.level.addParticle(Particles.WATER_RIPPLE(), this.x, pos.getY() + fluidState.getHeight(this.level, pos), this.z, 0.0, 0.0, 0.0);
                }
                this.remove();
            }
        }
    }

    private void tickWaterfallSpray() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
        if (this.onGround) {
            if (this.random.nextFloat() < 0.5F) {
                this.remove();
            }

            this.xd *= 0.7F;
            this.zd *= 0.7F;
        }

        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        double collisionHeight = this.level.getBlockState(pos)
                .getCollisionShape(this.level, pos)
                .max(net.minecraft.core.Direction.Axis.Y, this.x - pos.getX(), this.z - pos.getZ());
        FluidState fluidState = this.level.getFluidState(pos);
        double fluidHeight = fluidState.getHeight(this.level, pos);
        double height = Math.max(collisionHeight, fluidHeight);
        if (height > 0.0 && this.y < pos.getY() + height) {
            if (fluidState.is(Fluids.WATER) && ParticularConfig.waterDripRipples()) {
                this.level.addParticle(Particles.WATER_RIPPLE(), this.x, pos.getY() + fluidHeight, this.z, 0.0, 0.0, 0.0);
            }
            this.remove();
        }
    }

    private void afterMove() {
        if (this.onGround) {
            this.remove();
            this.level.addParticle(this.mode == Mode.LAVA ? ParticleTypes.LANDING_LAVA : ParticleTypes.SPLASH, this.x, this.y, this.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTick) {
        Vec3 cameraPos = camera.position();
        float x = (float)(this.xo + (this.x - this.xo) * partialTick - cameraPos.x());
        float y = (float)(this.yo + (this.y - this.yo) * partialTick - cameraPos.y());
        float z = (float)(this.zo + (this.z - this.zo) * partialTick - cameraPos.z());
        int color = ARGB.colorFromFloat(1.0F, this.rCol, this.gCol, this.bCol);
        int light = this.getLightCoords(partialTick);

        addFace(state, x, y, z + HALF_SIDE, IDENTITY, color, light);
        addFace(state, x, y, z - HALF_SIDE, BACK, color, light);
        addFace(state, x + HALF_SIDE, y, z, EAST, color, light);
        addFace(state, x - HALF_SIDE, y, z, WEST, color, light);
        addFace(state, x, y + HALF_SIDE, z, UP, color, light);
        addFace(state, x, y - HALF_SIDE, z, DOWN, color, light);
    }

    private void addFace(QuadParticleRenderState state, float x, float y, float z, Quaternionf rotation, int color, int light) {
        state.add(
                this.getLayer(),
                x, y, z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                HALF_SIDE,
                this.getU0(), this.getU1(), this.getV0(), this.getV1(),
                color,
                light
        );
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public static class Factory implements ParticleProvider<Options> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(Options options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource random) {
            return new CuboidParticle(level, x, y, z, xd, yd, zd, this.sprites.first(), options);
        }
    }

    public static Options waterfallSpray() {
        return new Options(Mode.WATERFALL_SPRAY, ParticularConfig.COMMON.waterCuboidColor.get(), ParticularConfig.COMMON.waterCuboidBiomeTint.get());
    }

    public static Options whiteSplash() {
        return new Options(Mode.SPLASH_WATER, 0xFFFFFF, false);
    }

    public static Options lava() {
        return new Options(Mode.LAVA, ParticularConfig.COMMON.lavaCuboidColor.get(), false);
    }

    private static int color(ClientLevel level, double x, double y, double z, Options options) {
        if (options.biomeTint()) {
            return BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z));
        }

        return options.color();
    }
}
