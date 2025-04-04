package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

@Mixin(value = Entity.class)
public abstract class InjectEntity
{
	@Shadow private Vec3 deltaMovement;
	@Shadow private EntityDimensions dimensions;
	@Shadow @Final protected RandomSource random;
	@Shadow public abstract double getX();
	@Shadow public abstract double getY();
	@Shadow public abstract double getZ();

	@Shadow public abstract Level level();
	@Shadow public abstract Vec3 position();
	@Shadow public abstract BlockPos blockPosition();

	@Shadow private Level level;
	@Shadow private BlockPos blockPosition;
	@Unique
	public Queue<Double> velocities = new LinkedList<>();

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	private void onSetVelocity(CallbackInfo ci)
	{
		if (!ParticularConfig.waterSplash()) { return; }

		velocities.offer(Math.abs(deltaMovement.y()));
		if (velocities.size() > 4)
		{
			velocities.poll();
		}
	}

	@Inject(
		method = "doWaterSplashEffect",
		at = @At("TAIL"))
	private void waterParticles(CallbackInfo ci)
	{
		if (!ParticularConfig.waterSplash()) { return; }

		//noinspection ConstantConditions
		if ((Object) this instanceof Arrow || !level().isClientSide) { return; }

		// Find water height
		float baseY = Mth.floor(getY());

		boolean foundSurface = false;
		FluidState prevState = Fluids.EMPTY.defaultFluidState();
		for (int i = 0; i < 5; ++i)
		{
			FluidState nextState = level().getFluidState(blockPosition().offset(0, i, 0));
			if ((prevState.is(Fluids.WATER) ||
					prevState.is(ForgeRegistries.FLUIDS.tags().createTagKey(new ResourceLocation("tfc:any_water"))))
					&& nextState.is(Fluids.EMPTY))
			{
				baseY += i - 1;
				foundSurface = true;
				break;
			}

			prevState = nextState;
		}

		if (!foundSurface) { return; }

		double velocityValue = velocities.isEmpty() ? 0.0f : Collections.max(velocities);

		// 3D splash
		level().addParticle(Particles.WATER_SPLASH_EMITTER.get(), getX(), baseY + prevState.getOwnHeight(), getZ(), dimensions.width, velocityValue, 0.0);
	}
}