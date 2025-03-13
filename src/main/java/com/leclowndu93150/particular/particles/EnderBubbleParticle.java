package com.leclowndu93150.particular.particles;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.mixin.AccessorBillboardParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderBubbleParticle extends TextureSheetParticle
{
	protected EnderBubbleParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i)
	{
		super(clientWorld, d, e, f);
		this.gravity = -0.125F;
		this.friction = 0.85F;
		this.setSize(0.02F, 0.02F);
		this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
		this.xd = g * 0.2F + (Math.random() * 2.0F - 1.0F) * 0.02F;
		this.yd = h * 0.2F + (Math.random() * 2.0F - 1.0F) * 0.02F;
		this.zd = i * 0.2F + (Math.random() * 2.0F - 1.0F) * 0.02F;
		this.lifetime = (int)(40.0F / (Math.random() * 0.8D + 0.2D));
	}

	@Override
	public void tick()
	{
		super.tick();

		if (!this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
			this.remove();
		}

		if (!ParticularConfig.poppingBubbles()) { return; }

		if (this.removed)
		{
			Particle bubble = Minecraft.getInstance().particleEngine.createParticle(Particles.ENDER_BUBBLE_POP.get(), x, y, z, 0, 0, 0);
			if (bubble != null)
			{
				((AccessorBillboardParticle) bubble).setQuadSize(this.quadSize * 2f);
			}
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
			EnderBubbleParticle enderBubbleParticle = new EnderBubbleParticle(clientWorld, d, e, f, g, h, i);
			enderBubbleParticle.pickSprite(this.spriteProvider);
			return enderBubbleParticle;
		}
	}
}