package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class Rainbow {
    private static final float RAIN_THRESHOLD = 0.5f;
    private static final int FADE_IN_TICKS = 200;
    private static final int FADE_OUT_TICKS = 400;
    private static final float MAX_SUN_ELEVATION = 20f * Mth.DEG_TO_RAD;
    private static final float DAWN_ELEVATION = -2f * Mth.DEG_TO_RAD;
    private static final float DAY_ELEVATION = 6f * Mth.DEG_TO_RAD;
    private static final float PRIMARY_INNER = 37f;
    private static final float PRIMARY_OUTER = 44f;
    private static final float SECONDARY_INNER = 50f;
    private static final float SECONDARY_OUTER = 57f;
    private static final float GLOW_START = 18f;
    private static final float GLOW_END = 38.5f;
    private static final int PRIMARY_RINGS = 16;
    private static final int SECONDARY_RINGS = 12;
    private static final int GLOW_RINGS = 6;
    private static final float SECONDARY_RATIO = 0.35f;
    private static final float GLOW_RATIO = 0.22f;
    private static final float DARK_RATIO = 0.18f;
    private static final float LUMINOSITY = 0.2f;
    private static final float HORIZON_FADE = 0.12f;
    private static final float HORIZON_DEPTH = 0.1f;
    private static final float ANCHOR_DISTANCE = 1800f;
    private static final float RENDER_RADIUS = 100f;
    private static final int SEGMENTS = 96;

    private static final float[] SPECTRUM_STOPS = {0f, 0.18f, 0.36f, 0.52f, 0.68f, 0.84f, 1f};
    private static final float[][] SPECTRUM = {
            {0.50f, 0.20f, 0.85f},
            {0.25f, 0.35f, 0.95f},
            {0.20f, 0.70f, 0.95f},
            {0.25f, 0.90f, 0.35f},
            {1.00f, 0.95f, 0.25f},
            {1.00f, 0.55f, 0.15f},
            {0.95f, 0.18f, 0.15f},
    };
    private static final float[] WHITE = {1f, 1f, 1f};
    private static final float[] BLACK = {0f, 0f, 0f};

    private static final float[] SEGMENT_COS = new float[SEGMENTS + 1];
    private static final float[] SEGMENT_SIN = new float[SEGMENTS + 1];

    static {
        for (int i = 0; i <= SEGMENTS; i++) {
            SEGMENT_COS[i] = Mth.cos(i * Mth.TWO_PI / SEGMENTS);
            SEGMENT_SIN[i] = Mth.sin(i * Mth.TWO_PI / SEGMENTS);
        }
    }

    private static boolean wasRaining;
    private static int duration;
    private static int ticksLeft;
    private static Vec3 origin = Vec3.ZERO;
    private static float axisX;
    private static float axisY;

    public static void tick(ClientLevel level) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused()) return;

        if (!ParticularConfig.rainbows()) {
            ticksLeft = 0;
            wasRaining = false;
            return;
        }

        boolean raining = level.getRainLevel(1f) > RAIN_THRESHOLD;
        if (wasRaining && !raining && rainedHere(level, mc.player.blockPosition())
                && level.random.nextDouble() < ParticularConfig.COMMON.rainbowChance.get()) {
            place(level, mc);
        }
        wasRaining = raining;

        if (ticksLeft > 0) {
            ticksLeft--;
        }
    }

    public static void clear() {
        ticksLeft = 0;
        wasRaining = false;
    }

    private static void place(ClientLevel level, Minecraft mc) {
        float sunAngle = level.getSunAngle(1f);
        float elevation = (float) Math.asin(Mth.clamp(Mth.cos(sunAngle), -1f, 1f));
        if (elevation < DAWN_ELEVATION) return;

        float bowElevation = Math.min(elevation, MAX_SUN_ELEVATION);
        float side = -Mth.sin(sunAngle) >= 0f ? 1f : -1f;
        axisX = -side * Mth.cos(bowElevation);
        axisY = -Mth.sin(bowElevation);
        origin = mc.player.getEyePosition();
        duration = ParticularConfig.COMMON.rainbowDuration.get() * 20;
        ticksLeft = duration;
    }

    private static boolean rainedHere(ClientLevel level, BlockPos pos) {
        return level.dimensionType().hasSkyLight()
                && level.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN;
    }

    public static void render(Matrix4f frustumMatrix, float partialTick, Camera camera, boolean foggy) {
        if (ticksLeft <= 0 || foggy) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || level.effects().skyType() != DimensionSpecialEffects.SkyType.NORMAL) return;

        FogType fog = camera.getFluidInCamera();
        if (fog == FogType.POWDER_SNOW || fog == FogType.LAVA) return;

        float elevation = (float) Math.asin(Mth.clamp(Mth.cos(level.getSunAngle(partialTick)), -1f, 1f));

        float age = duration - ticksLeft + partialTick;
        float strength = Math.min(1f, Math.min(age / FADE_IN_TICKS, (float) ticksLeft / FADE_OUT_TICKS));
        strength *= 1f - level.getRainLevel(partialTick);
        strength *= Mth.clamp((elevation - DAWN_ELEVATION) / (DAY_ELEVATION - DAWN_ELEVATION), 0f, 1f);
        strength *= ParticularConfig.COMMON.rainbowOpacity.get().floatValue();
        if (strength <= 0.01f) return;

        Vec3 offset = origin.subtract(camera.getPosition());
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Bow bow = new Bow(buffer, frustumMatrix, axisX, axisY, -axisY, axisX,
                (float) offset.x, (float) offset.y, (float) offset.z);

        float darkMiddle = (PRIMARY_OUTER + SECONDARY_INNER) * 0.5f;
        float dark = strength * DARK_RATIO;
        bow.ring(PRIMARY_OUTER, darkMiddle, BLACK, 0f, BLACK, dark);
        bow.ring(darkMiddle, SECONDARY_INNER, BLACK, dark, BLACK, 0f);

        float glow = strength * GLOW_RATIO;
        for (int i = 0; i < GLOW_RINGS; i++) {
            float from = (float) i / GLOW_RINGS;
            float to = (float) (i + 1) / GLOW_RINGS;
            bow.ring(Mth.lerp(from, GLOW_START, PRIMARY_INNER), Mth.lerp(to, GLOW_START, PRIMARY_INNER),
                    WHITE, glow * smoothstep(from), WHITE, glow * smoothstep(to));
        }
        bow.ring(PRIMARY_INNER, GLOW_END, WHITE, glow, WHITE, 0f);

        spectrumBand(bow, PRIMARY_INNER, PRIMARY_OUTER, PRIMARY_RINGS, strength, false);
        spectrumBand(bow, SECONDARY_INNER, SECONDARY_OUTER, SECONDARY_RINGS, strength * SECONDARY_RATIO, true);

        BufferBuilder.RenderedBuffer mesh = buffer.endOrDiscardIfEmpty();
        if (mesh == null) return;

        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        BufferUploader.drawWithShader(mesh);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
    }

    private static void spectrumBand(Bow bow, float inner, float outer, int rings, float strength, boolean reversed) {
        float[] fromColour = new float[3];
        float[] toColour = new float[3];
        for (int i = 0; i < rings; i++) {
            float from = (float) i / rings;
            float to = (float) (i + 1) / rings;
            float fromSpectrum = reversed ? 1f - from : from;
            float toSpectrum = reversed ? 1f - to : to;
            spectrum(fromSpectrum, fromColour);
            spectrum(toSpectrum, toColour);
            bow.ring(Mth.lerp(from, inner, outer), Mth.lerp(to, inner, outer),
                    fromColour, strength * envelope(fromSpectrum), toColour, strength * envelope(toSpectrum));
        }
    }

    private static void spectrum(float position, float[] colour) {
        int index = 0;
        while (index < SPECTRUM_STOPS.length - 2 && position > SPECTRUM_STOPS[index + 1]) {
            index++;
        }

        float blend = Mth.clamp((position - SPECTRUM_STOPS[index]) / (SPECTRUM_STOPS[index + 1] - SPECTRUM_STOPS[index]), 0f, 1f);
        for (int channel = 0; channel < 3; channel++) {
            float pure = Mth.lerp(blend, SPECTRUM[index][channel], SPECTRUM[index + 1][channel]);
            colour[channel] = pure * (1f - LUMINOSITY) + LUMINOSITY;
        }
    }

    private static float envelope(float position) {
        return smoothstep(position / 0.25f) * (1f - smoothstep((position - 0.88f) / 0.12f));
    }

    private static float smoothstep(float value) {
        float t = Mth.clamp(value, 0f, 1f);
        return t * t * (3f - 2f * t);
    }

    private record Bow(BufferBuilder buffer, Matrix4f matrix, float axisX, float axisY, float upX, float upY,
                       float offsetX, float offsetY, float offsetZ) {
        void ring(float innerDegrees, float outerDegrees, float[] innerColour, float innerAlpha, float[] outerColour, float outerAlpha) {
            float innerAlong = Mth.cos(innerDegrees * Mth.DEG_TO_RAD);
            float innerOut = Mth.sin(innerDegrees * Mth.DEG_TO_RAD);
            float outerAlong = Mth.cos(outerDegrees * Mth.DEG_TO_RAD);
            float outerOut = Mth.sin(outerDegrees * Mth.DEG_TO_RAD);

            for (int segment = 0; segment < SEGMENTS; segment++) {
                float highest = Math.max(
                        Math.max(height(innerAlong, innerOut, segment), height(outerAlong, outerOut, segment)),
                        Math.max(height(innerAlong, innerOut, segment + 1), height(outerAlong, outerOut, segment + 1)));
                if (highest <= -HORIZON_DEPTH * 2f * ANCHOR_DISTANCE) continue;

                vertex(innerAlong, innerOut, segment, innerColour, innerAlpha);
                vertex(outerAlong, outerOut, segment, outerColour, outerAlpha);
                vertex(outerAlong, outerOut, segment + 1, outerColour, outerAlpha);
                vertex(innerAlong, innerOut, segment + 1, innerColour, innerAlpha);
            }
        }

        private float height(float along, float out, int segment) {
            return offsetY + (axisY * along + upY * SEGMENT_COS[segment] * out) * ANCHOR_DISTANCE / along;
        }

        private void vertex(float along, float out, int segment, float[] colour, float alpha) {
            float scale = ANCHOR_DISTANCE / along;
            float x = offsetX + (axisX * along + upX * SEGMENT_COS[segment] * out) * scale;
            float y = offsetY + (axisY * along + upY * SEGMENT_COS[segment] * out) * scale;
            float z = offsetZ + SEGMENT_SIN[segment] * out * scale;
            float toSphere = RENDER_RADIUS / Mth.sqrt(x * x + y * y + z * z);
            float steepness = y * toSphere / RENDER_RADIUS;
            buffer.vertex(matrix, x * toSphere, y * toSphere, z * toSphere)
                    .color(colour[0], colour[1], colour[2], alpha * Mth.clamp((steepness + HORIZON_DEPTH) / HORIZON_FADE, 0f, 1f))
                    .endVertex();
        }
    }
}
