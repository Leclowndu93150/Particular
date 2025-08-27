package com.leclowndu93150.particular.render;

import com.leclowndu93150.particular.Particles;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import java.awt.*;

public class LodestoneEffects {

    public static void createEnderPearlTrailEffect(ThrownEnderpearl enderPearl, float deltaTime) {
        float posX = (float) Mth.lerp(deltaTime, enderPearl.xo, enderPearl.getX());
        float posY = (float) Mth.lerp(deltaTime, enderPearl.yo, enderPearl.getY());
        float posZ = (float) Mth.lerp(deltaTime, enderPearl.zo, enderPearl.getZ());

        Vec3 motion = enderPearl.getDeltaMovement();
        if (motion.length() > 0.05) {
            spawnEnderPearlTrailParticles(enderPearl, posX, posY, posZ, motion);
        }
    }

    private static void spawnEnderPearlTrailParticles(ThrownEnderpearl enderPearl, float x, float y, float z, Vec3 velocity) {
        for (int i = 0; i < 3; i++) {
            double trailDistance = (i + 1) * 0.4;
            double trailX = x - velocity.x * trailDistance;
            double trailY = y - velocity.y * trailDistance;
            double trailZ = z - velocity.z * trailDistance;

            float trailScale = 0.15f - i * 0.03f;
            float alpha = 0.8f - i * 0.15f;

            WorldParticleBuilder.create(new WorldParticleOptions(Particles.ENDER_PEARL_TRAIL.get()))
                    .setSpinData(SpinParticleData.create((float)(enderPearl.level().random.nextGaussian() * 0.2f)).build())
                    .setScaleData(GenericParticleData.create(trailScale, 0f).setEasing(Easing.EXPO_OUT).build())
                    .setTransparencyData(GenericParticleData.create(alpha, 0f).setEasing(Easing.QUAD_OUT).build())
                    .setColorData(
                            ColorParticleData.create(new Color(0x22CFFF), new Color(0x8A2BE2))
                                    .setEasing(Easing.SINE_IN_OUT)
                                    .build()
                    )
                    .enableNoClip()
                    .setLifetime(12 + enderPearl.level().random.nextInt(8))
                    .spawn(
                            enderPearl.level(),
                            trailX + enderPearl.level().random.nextGaussian() * 0.08,
                            trailY + (enderPearl.getBbHeight() * 0.5) + enderPearl.level().random.nextGaussian() * 0.08,
                            trailZ + enderPearl.level().random.nextGaussian() * 0.08
                    );
        }
    }
}
