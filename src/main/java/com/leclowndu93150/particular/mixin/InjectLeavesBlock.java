package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LeavesBlock.class)
public class InjectLeavesBlock
{
	@Inject(
		method = "animateTick",
		at = @At("HEAD"), remap = false)
	private void dropLeaves(BlockState state, Level world, BlockPos pos, RandomSource random, CallbackInfo ci)
	{
		if (!ParticularConfig.fallingLeaves()) { return; }

		if (random.nextInt(ParticularConfig.COMMON.fallingLeavesSpawnChance.get()) == 0)
		{
			BlockPos blockPos = pos.below();
			BlockState blockState = world.getBlockState(blockPos);
			if (!Block.isFaceFull(blockState.getCollisionShape(world, blockPos), Direction.UP))
			{
				double x = pos.getX() + 0.02d + random.nextDouble() * 0.96d;
				double y = pos.getY() - 0.05d;
				double z = pos.getZ() + 0.02d + random.nextDouble() * 0.96d;

				Main.LeafData leafData = Main.getLeafData(state.getBlock());

				ParticleOptions particle = leafData.getParticle();
				if (particle == null) { return; }
				Color color = leafData.getColor(world, pos);

				Particle leaf = Minecraft.getInstance().particleEngine.createParticle(particle, x, y, z, 0, 0, 0);
				if (leaf != null)
				{
					leaf.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
				}
			}
		}
	}
}