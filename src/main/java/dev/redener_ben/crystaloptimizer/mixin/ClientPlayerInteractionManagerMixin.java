package dev.redener_ben.crystaloptimizer.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void crystaloptimizer$onAttackEntity(Player player, Entity target, CallbackInfo ci) {
        if (target instanceof EndCrystal) {
            target.setRemoved(Entity.RemovalReason.KILLED);
            target.onClientRemoval();
        }
    }
}
