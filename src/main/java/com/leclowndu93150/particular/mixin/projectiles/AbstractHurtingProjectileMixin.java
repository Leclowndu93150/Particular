package com.leclowndu93150.particular.mixin.projectiles;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractHurtingProjectile.class)
public class AbstractHurtingProjectileMixin {
    
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void cancelTrailParticle(Level level, ParticleOptions particleOptions, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        AbstractHurtingProjectile self = (AbstractHurtingProjectile) (Object) this;
        if (self instanceof Fireball && ParticularConfig.fireballFlames()) {
            return;
        }
        level.addParticle(particleOptions, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}