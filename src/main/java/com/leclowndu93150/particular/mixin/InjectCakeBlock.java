package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class InjectCakeBlock
{
	@Inject(at = @At("TAIL"), method = "eat", remap = false)
	private static void makeSounds(LevelAccessor world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (!ParticularConfig.cakeEatingParticles()) { return; }

		ItemStack cake = Items.CAKE.getDefaultInstance();
		player.playSound(SoundEvents.GENERIC_EAT, 1f, 1f);
		((InvokerLivingEntity) player).spawnParticles(cake, 5);
	}
}