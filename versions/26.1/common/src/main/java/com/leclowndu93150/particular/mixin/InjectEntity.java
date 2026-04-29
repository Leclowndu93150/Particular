package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class InjectEntity
{
	@Unique
	private static final double LARGE_WATER_SPLASH_MIN_FALL_DISTANCE = 1.0;
	@Unique
	private static final double LARGE_WATER_SPLASH_MIN_DOWNWARD_SPEED = 0.35;
	@Unique
	private static final int SOFT_WATER_PARTICLE_COUNT = 12;

	@Shadow private Vec3 deltaMovement;
	@Shadow private EntityDimensions dimensions;
	@Shadow @Final protected RandomSource random;
	@Shadow public abstract double getX();
	@Shadow public abstract double getY();
	@Shadow public abstract double getZ();

	@Shadow public abstract Level level();
	@Shadow public abstract Vec3 position();
	@Shadow public abstract BlockPos blockPosition();
	@Shadow public abstract boolean isInLava();

	@Shadow private Level level;
	@Shadow private BlockPos blockPosition;
	@Shadow public double fallDistance;
	@Unique
	public Queue<Double> velocities = new LinkedList<>();
	@Unique
	private double accumulatedFallDistance = 0.0;
	@Unique
	private boolean wasInLava = false;

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	private void onSetVelocity(CallbackInfo ci)
	{
		if (!ParticularConfig.waterSplash() && !ParticularConfig.lavaSplash()) { return; }

		velocities.offer(Math.abs(deltaMovement.y()));
		if (velocities.size() > 4)
		{
			velocities.poll();
		}

		if (deltaMovement.y() < 0.0) {
			accumulatedFallDistance += Math.abs(deltaMovement.y());
		} else if (deltaMovement.y() > 0.0) {
			accumulatedFallDistance = 0.0;
		}

		if (ParticularConfig.lavaSplash() && level().isClientSide()) {
			boolean inLava = isInLava();
			if (inLava && !wasInLava) {
				lavaParticles();
			}
			wasInLava = inLava;
		}
	}

	@Inject(
		method = "doWaterSplashEffect",
		at = @At("TAIL"))
	private void waterParticles(CallbackInfo ci)
	{
		if (!ParticularConfig.waterSplash()) { return; }

		//noinspection ConstantConditions
		if ((Object) this instanceof Arrow || !level().isClientSide()) { return; }

		// Find water height
		float baseY = Mth.floor(getY());

		boolean foundSurface = false;
		FluidState prevState = Fluids.EMPTY.defaultFluidState();
		for (int i = 0; i < 5; ++i)
		{
			FluidState nextState = level().getFluidState(blockPosition().offset(0, i, 0));
			if (prevState.is(Fluids.WATER) && nextState.is(Fluids.EMPTY))
			{
				baseY += i - 1;
				foundSurface = true;
				break;
			}

			prevState = nextState;
		}

		if (!foundSurface) { return; }

		double velocityValue = velocities.isEmpty() ? 0.0f : Collections.max(velocities);

		double surfaceY = baseY + prevState.getOwnHeight();
		double actualFallDistance = fallDistance > 0.0 ? fallDistance : accumulatedFallDistance;
		double downwardSpeed = -deltaMovement.y();

		double minFallDistance = Math.max(ParticularConfig.COMMON.waterSplashMinFallDistance.get(), LARGE_WATER_SPLASH_MIN_FALL_DISTANCE);
		if (actualFallDistance < minFallDistance || downwardSpeed < LARGE_WATER_SPLASH_MIN_DOWNWARD_SPEED) {
			if (ParticularConfig.COMMON.waterSplashSoftEntryParticles.get()) {
				softWaterEntryParticles(surfaceY, dimensions.width(), velocityValue);
			}
			accumulatedFallDistance = 0.0;
			return; 
		}

		accumulatedFallDistance = 0.0;

		level().addParticle(Particles.WATER_SPLASH_EMITTER(), getX(), surfaceY, getZ(), dimensions.width(), velocityValue, 0.0);
	}

	@Unique
	private void softWaterEntryParticles(double surfaceY, float width, double speed)
	{
		double radius = Math.max(0.25, width * 0.5);
		int count = Math.max(4, (int)(SOFT_WATER_PARTICLE_COUNT * width));
		for (int i = 0; i < count; i++) {
			double xOffset = random.triangle(0.0, radius);
			double zOffset = random.triangle(0.0, radius);
			double x = getX() + xOffset;
			double y = surfaceY + random.nextDouble() * 0.08;
			double z = getZ() + zOffset;

			if (random.nextBoolean()) {
				level().addParticle(ParticleTypes.BUBBLE, x, y - 0.08, z, xOffset * 0.04, 0.02 + random.nextDouble() * 0.03, zOffset * 0.04);
			} else {
				double dropletSpeed = 0.02 + Math.min(speed, 0.25) * 0.08;
				level().addParticle(ParticleTypes.FALLING_WATER, x, y + 0.02, z, xOffset * 0.02, dropletSpeed, zOffset * 0.02);
			}
		}
	}

	@Unique
	private void lavaParticles()
	{
		//noinspection ConstantConditions
		if ((Object) this instanceof Arrow) { return; }

		float baseY = Mth.floor(getY());

		boolean foundSurface = false;
		FluidState prevState = Fluids.EMPTY.defaultFluidState();
		for (int i = 0; i < 5; ++i)
		{
			FluidState nextState = level().getFluidState(blockPosition().offset(0, i, 0));
			if (prevState.is(Fluids.LAVA) && nextState.is(Fluids.EMPTY))
			{
				baseY += i - 1;
				foundSurface = true;
				break;
			}

			prevState = nextState;
		}

		if (!foundSurface) { return; }

		double velocityValue = velocities.isEmpty() ? 0.0f : Collections.max(velocities);

		double actualFallDistance = fallDistance > 0.0 ? fallDistance : accumulatedFallDistance;

		if (actualFallDistance < ParticularConfig.COMMON.waterSplashMinFallDistance.get()) {
			accumulatedFallDistance = 0.0;
			return;
		}

		accumulatedFallDistance = 0.0;

		level().addParticle(Particles.WATER_SPLASH_EMITTER(), getX(), baseY + prevState.getOwnHeight(), getZ(), dimensions.width(), velocityValue, 1.0);
	}
}
