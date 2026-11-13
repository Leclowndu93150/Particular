package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ColdBreath {
    private static final int BREATH_PERIOD = 70;
    private static final int SPRINT_BREATH_PERIOD = 30;
    private static final int EXHALE_TICKS = 10;
    private static final int PUFF_INTERVAL = 2;
    private static final int PHASE_SPREAD = 17;
    private static final double RANGE_SQ = 32.0 * 32.0;
    private static final double EXHALE_SPEED = 0.03;
    private static final double RISE_SPEED = 0.005;
    private static final double JITTER = 0.006;

    public static void tick(ClientLevel level) {
        if (!ParticularConfig.coldBreath()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused()) return;

        boolean mobs = ParticularConfig.COMMON.coldBreathMobs.get();
        boolean hideOwn = mc.options.getCameraType().isFirstPerson() && !ParticularConfig.COMMON.coldBreathFirstPerson.get();

        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!mobs && !(living instanceof Player)) continue;

            int period = living.isSprinting() ? SPRINT_BREATH_PERIOD : BREATH_PERIOD;
            int phase = (living.tickCount + living.getId() * PHASE_SPREAD) % period;
            if (phase >= EXHALE_TICKS || phase % PUFF_INTERVAL != 0) continue;

            if (hideOwn && living == mc.player) continue;
            if (living.distanceToSqr(mc.player) > RANGE_SQ) continue;
            if (!breathes(living)) continue;

            BlockPos pos = BlockPos.containing(living.getEyePosition());
            if (!level.getBiome(pos).value().coldEnoughToSnow(pos)) continue;

            exhale(level, living);
        }
    }

    private static boolean breathes(LivingEntity living) {
        return living.isAlive()
                && !(living instanceof ArmorStand)
                && !living.isInvisible()
                && !living.isSpectator()
                && !living.isUnderWater()
                && !living.canBreatheUnderwater()
                && living.getMobType() != MobType.UNDEAD;
    }

    private static void exhale(ClientLevel level, LivingEntity living) {
        RandomSource random = level.random;
        Vec3 look = Vec3.directionFromRotation(living.getXRot(), living.getYHeadRot());
        Vec3 mouth = living.getEyePosition()
                .add(look.scale(living.getBbWidth() * 0.5 + 0.1))
                .add(0.0, -living.getBbHeight() * 0.08, 0.0);

        level.addParticle(Particles.BREATH(), mouth.x, mouth.y, mouth.z,
                look.x * EXHALE_SPEED + (living.getX() - living.xo) + random.nextDouble() * JITTER * 2 - JITTER,
                look.y * EXHALE_SPEED + RISE_SPEED,
                look.z * EXHALE_SPEED + (living.getZ() - living.zo) + random.nextDouble() * JITTER * 2 - JITTER);
    }
}
