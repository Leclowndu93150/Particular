package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class InjectWaterFluid
{
	@Inject(
		method = "animateTick",
		at = @At("TAIL"), remap = false)
	private void waterParticles(Level world, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci)
	{
		if (!ParticularConfig.waterfallSpray()) { return; }

		if (!state.isSource() &&
			world.getFluidState(pos.below()).is(FluidTags.WATER))
		{
			// Splishy splashy
			for (int i = 0; i < 2; ++i)
			{
				if (state.getValue(BlockStateProperties.FALLING))
				{
					double x = pos.getX();
					double y = (double) pos.getY() + random.nextDouble();
					double z = pos.getZ();

					if (random.nextBoolean())
					{
						x += random.nextDouble();
						z += random.nextIntBetweenInclusive(0, 1);
					}
					else
					{
						x += random.nextIntBetweenInclusive(0, 1);
						z += random.nextDouble();
					}

					world.addParticle(Particles.WATERFALL_SPRAY.get(), x, y, z, 0.0, 0.0, 0.0);
				}
				else
				{
					double x = (double) pos.getX() + random.nextDouble();
					double y = (double) pos.getY() + (random.nextDouble() * state.getOwnHeight());
					double z = (double) pos.getZ() + random.nextDouble();
					Vec3 vel = state.getFlow(world, pos).scale(0.075);
					world.addParticle(Particles.WATERFALL_SPRAY.get(), x, y, z, vel.x, 0.0, vel.z);
				}
			}
		}
	}
}