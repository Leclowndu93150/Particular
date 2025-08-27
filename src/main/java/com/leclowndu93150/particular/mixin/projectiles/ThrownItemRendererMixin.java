package com.leclowndu93150.particular.mixin.projectiles;

import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.render.LodestoneEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemRenderer.class)
public class ThrownItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), remap = false, cancellable = true)
    private void onRenderHead(Entity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof ThrownEnderpearl enderPearl && ParticularConfig.enderPearlTrails()) {
            LodestoneEffects.createEnderPearlTrailEffect(enderPearl, partialTicks);
        }
    }
}