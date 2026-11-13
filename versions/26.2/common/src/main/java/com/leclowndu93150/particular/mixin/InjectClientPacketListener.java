package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.utils.FootstepDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class InjectClientPacketListener
{
	@Inject(
		method = "handleParticleEvent",
		at = @At("HEAD"),
		cancellable = true)
	private void replaceLandingBurst(ClientboundLevelParticlesPacket packet, CallbackInfo ci)
	{
		if (!Minecraft.getInstance().isSameThread()) { return; }

		if (FootstepDust.replaceLandingBurst(packet))
		{
			ci.cancel();
		}
	}
}
