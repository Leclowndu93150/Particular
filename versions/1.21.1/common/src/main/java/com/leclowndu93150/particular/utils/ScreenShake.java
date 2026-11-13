package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ScreenShake {
    private static final float DEGREES_PER_INTENSITY = 1.5f;
    private static final float MAX_DEGREES = 4f;
    private static final float ATTACK_TICKS = 2f;
    private static final float YAW_FREQUENCY = 2.8f;
    private static final float PITCH_FREQUENCY = 3.7f;

    private static final List<Shake> SHAKES = new CopyOnWriteArrayList<>();
    private static List<? extends String> parsedFrom;
    private static Map<ResourceLocation, Settings> settings = Map.of();

    private static float yawOffset;
    private static float pitchOffset;

    public static void onSound(SoundInstance sound) {
        if (!ParticularConfig.screenShake() || sound.isRelative()) return;

        Settings match = settings().get(sound.getLocation());
        if (match == null) return;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        SHAKES.add(new Shake(
                new Vec3(sound.getX(), sound.getY(), sound.getZ()),
                match,
                random.nextFloat() * Mth.TWO_PI,
                random.nextFloat() * Mth.TWO_PI));
    }

    public static void tick() {
        if (SHAKES.isEmpty() || Minecraft.getInstance().isPaused()) return;

        SHAKES.removeIf(shake -> ++shake.age >= shake.settings.duration);
    }

    public static void clear() {
        SHAKES.clear();
    }

    public static void update(Vec3 cameraPos, float partialTick) {
        yawOffset = 0f;
        pitchOffset = 0f;
        if (SHAKES.isEmpty() || !ParticularConfig.screenShake()) return;

        float yaw = 0f;
        float pitch = 0f;
        for (Shake shake : SHAKES) {
            float time = shake.age + partialTick;
            float strength = shake.strength(cameraPos, time);
            if (strength <= 0f) continue;

            yaw += strength * Mth.sin(time * YAW_FREQUENCY + shake.yawPhase);
            pitch += strength * Mth.sin(time * PITCH_FREQUENCY + shake.pitchPhase);
        }

        float scale = DEGREES_PER_INTENSITY * ParticularConfig.COMMON.screenShakeIntensity.get().floatValue();
        yawOffset = Mth.clamp(yaw * scale, -MAX_DEGREES, MAX_DEGREES);
        pitchOffset = Mth.clamp(pitch * scale, -MAX_DEGREES, MAX_DEGREES);
    }

    public static float yawOffset() {
        return yawOffset;
    }

    public static float pitchOffset() {
        return pitchOffset;
    }

    private static Map<ResourceLocation, Settings> settings() {
        List<? extends String> entries = ParticularConfig.COMMON.screenShakeSounds.get();
        if (entries != parsedFrom) {
            Map<ResourceLocation, Settings> parsed = new HashMap<>();
            for (String entry : entries) {
                String[] parts = entry.split("\\|");
                if (parts.length != 4) continue;

                ResourceLocation id = ResourceLocation.tryParse(parts[0].trim());
                if (id == null) continue;

                try {
                    parsed.put(id, new Settings(
                            Float.parseFloat(parts[1].trim()),
                            Math.max(1, Integer.parseInt(parts[2].trim())),
                            Float.parseFloat(parts[3].trim())));
                } catch (NumberFormatException ignored) {
                }
            }
            settings = parsed;
            parsedFrom = entries;
        }
        return settings;
    }

    private record Settings(float intensity, int duration, float radius) {
    }

    private static class Shake {
        final Vec3 pos;
        final Settings settings;
        final float yawPhase;
        final float pitchPhase;
        int age;

        Shake(Vec3 pos, Settings settings, float yawPhase, float pitchPhase) {
            this.pos = pos;
            this.settings = settings;
            this.yawPhase = yawPhase;
            this.pitchPhase = pitchPhase;
        }

        float strength(Vec3 cameraPos, float time) {
            float falloff = 1f - (float) pos.distanceTo(cameraPos) / settings.radius;
            if (falloff <= 0f) return 0f;

            float fade = Math.max(0f, 1f - time / settings.duration);
            float attack = Math.min(1f, time / ATTACK_TICKS);
            return settings.intensity * falloff * fade * attack;
        }
    }
}
