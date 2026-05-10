package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.particles.CuboidParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class InjectEntity
{
	@Shadow private Vec3 deltaMovement;
	@Shadow private EntityDimensions dimensions;
	@Shadow @Final protected RandomSource random;
	@Shadow public abstract double getX();
	@Shadow public abstract double getY();
	@Shadow public abstract double getZ();

	@Shadow public abstract Level level();
	@Shadow public abstract BlockPos blockPosition();
	@Shadow public abstract boolean isInLava();
	@Shadow public float fallDistance;

	@Unique private final int particular$velArraySize = 4;
	@Unique private final double[] particular$vel = new double[particular$velArraySize];
	@Unique private int particular$velIdx = 0;
	@Unique private double particular$accumulatedFallDistance = 0.0;
	@Unique private boolean particular$wasInLava = false;

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	private void onSetVelocity(CallbackInfo ci)
	{
		if (!ParticularConfig.waterSplash() && !ParticularConfig.lavaSplash()) { return; }

		particular$vel[particular$velIdx % particular$velArraySize] = Math.abs(deltaMovement.y());
		particular$velIdx = (particular$velIdx + 1) % particular$velArraySize;

		if (deltaMovement.y() < 0.0) {
			particular$accumulatedFallDistance += Math.abs(deltaMovement.y());
		} else if (deltaMovement.y() > 0.0) {
			particular$accumulatedFallDistance = 0.0;
		}

		if (ParticularConfig.lavaSplash() && level().isClientSide) {
			boolean inLava = isInLava();
			if (inLava && !particular$wasInLava) {
				particular$lavaParticles();
			}
			particular$wasInLava = inLava;
		}
	}

	@Inject(
		method = "doWaterSplashEffect",
		at = @At("TAIL"))
	private void waterParticles(CallbackInfo ci)
	{
		if (!ParticularConfig.waterSplash()) { return; }

		if ((Object) this instanceof Arrow || !level().isClientSide) { return; }

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

		double velocityValue = Math.max(Math.max(particular$vel[0], particular$vel[1]), Math.max(particular$vel[2], particular$vel[3]));

		double actualFallDistance = fallDistance > 0.0 ? fallDistance : particular$accumulatedFallDistance;
		double surfaceY = baseY + prevState.getOwnHeight();
		double downwardSpeed = -deltaMovement.y();

		double minFallDistance = Math.max(ParticularConfig.COMMON.waterSplashMinFallDistance.get(), LARGE_WATER_SPLASH_MIN_FALL_DISTANCE);
		if (actualFallDistance < minFallDistance || downwardSpeed < LARGE_WATER_SPLASH_MIN_DOWNWARD_SPEED) {
			if (ParticularConfig.COMMON.waterSplashSoftEntryParticles.get()) {
				particular$softWaterEntryParticles(surfaceY, dimensions.width, velocityValue);
			}
			particular$accumulatedFallDistance = 0.0;
			return;
		}

		particular$accumulatedFallDistance = 0.0;

		level().addParticle(Particles.WATER_SPLASH_EMITTER(), getX(), surfaceY, getZ(), dimensions.width, velocityValue, 0.0);
	}

	@Unique
	private static final double LARGE_WATER_SPLASH_MIN_FALL_DISTANCE = 1.0;
	@Unique
	private static final double LARGE_WATER_SPLASH_MIN_DOWNWARD_SPEED = 0.35;
	@Unique
	private static final int SOFT_WATER_PARTICLE_COUNT = 12;

	@Unique
	private void particular$softWaterEntryParticles(double surfaceY, float width, double speed)
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
				level().addParticle(
						ParticularConfig.COMMON.cuboidSplashDroplets.get() ? CuboidParticle.whiteSplash() : ParticleTypes.FALLING_WATER,
						x, y + 0.02, z, xOffset * 0.02, dropletSpeed, zOffset * 0.02);
			}
		}
	}

	@Unique
	private void particular$lavaParticles()
	{
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

		double velocityValue = Math.max(Math.max(particular$vel[0], particular$vel[1]), Math.max(particular$vel[2], particular$vel[3]));

		double actualFallDistance = fallDistance > 0.0 ? fallDistance : particular$accumulatedFallDistance;

		if (actualFallDistance < ParticularConfig.COMMON.waterSplashMinFallDistance.get()) {
			particular$accumulatedFallDistance = 0.0;
			return;
		}

		particular$accumulatedFallDistance = 0.0;

		level().addParticle(Particles.WATER_SPLASH_EMITTER(), getX(), baseY + prevState.getOwnHeight(), getZ(), dimensions.width, velocityValue, 1.0);
	}
}
