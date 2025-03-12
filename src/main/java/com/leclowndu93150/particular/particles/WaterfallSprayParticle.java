package com.leclowndu93150.particular.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

public class WaterfallSprayParticle extends WaterDropParticle
{
	protected WaterfallSprayParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i)
	{
		super(clientWorld, d, e, f);

		xd += g;
		yd *= 0.75f;
		zd += i;

		Color color = new Color(BiomeColors.getAverageWaterColor(clientWorld, BlockPos.containing(x, y, z)));
		rCol = color.getRed() / 255f;
		gCol = color.getGreen() / 255f;
		bCol = color.getBlue() / 255f;
	}

	@Override
	public void tick()
	{
		xo = x;
		yo = y;
		zo = z;

		if (lifetime-- <= 0)
		{
			remove();
		}
		else
		{
			yd -= gravity;
			move(xd, yd, zd);
			xd *= 0.9800000190734863;
			yd *= 0.9800000190734863;
			zd *= 0.9800000190734863;

			if (onGround)
			{
				if (Math.random() < 0.5)
				{
					remove();
				}

				xd *= 0.699999988079071;
				zd *= 0.699999988079071;
			}

			BlockPos blockPos = BlockPos.containing(x, y, z);
			double d = level.getBlockState(blockPos).getCollisionShape(level, blockPos).max(Direction.Axis.Y, x - (double)blockPos.getX(), z - (double)blockPos.getZ());

			FluidState fluidState = level.getFluidState(blockPos);
			if (fluidState.isSource())
			{
				d = Math.max(d, fluidState.getHeight(level, blockPos));
			}

			if (d > 0.0 && y < (double)blockPos.getY() + d)
			{
				remove();
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i)
		{
			WaterfallSprayParticle waterfallSprayParticle = new WaterfallSprayParticle(clientWorld, d, e, f, g, h, i);
			waterfallSprayParticle.pickSprite(this.spriteProvider);
			return waterfallSprayParticle;
		}
	}
}