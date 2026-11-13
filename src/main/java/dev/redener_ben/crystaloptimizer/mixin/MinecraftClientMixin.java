package dev.redener_ben.crystaloptimizer.mixin;

import dev.redener_ben.crystaloptimizer.client.config.CrystalOptimizerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Shadow public LocalPlayer player;
    @Shadow public HitResult hitResult;
    @Shadow private int rightClickDelay;
    @Shadow protected int missTime;

    @Unique private long crystaloptimizer$lastCrystalAttackTime = 0L;

    @Unique
    private boolean crystaloptimizer$holdingCrystal() {
        return this.player != null
                && (this.player.getMainHandItem().is(Items.END_CRYSTAL) || this.player.getOffhandItem().is(Items.END_CRYSTAL));
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void crystaloptimizer$onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.hitResult != null && this.hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) this.hitResult).getEntity();
            if (entity instanceof EndCrystal) {
                long now = System.currentTimeMillis();
                if (now - this.crystaloptimizer$lastCrystalAttackTime < CrystalOptimizerConfig.getInstance().crystalHitDelayMs) {
                    cir.setReturnValue(false);
                    return;
                }
                this.crystaloptimizer$lastCrystalAttackTime = now;
                this.missTime = 0;
            }
        }
    }

    // Nur End Crystals bekommen Fast Place. Glowstone behaelt absichtlich die normale Verzoegerung.
    @Inject(method = "startUseItem", at = @At("RETURN"))
    private void crystaloptimizer$onDoItemUse(CallbackInfo ci) {
        if (CrystalOptimizerConfig.getInstance().fastPlace && this.crystaloptimizer$holdingCrystal()) {
            this.rightClickDelay = 0;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void crystaloptimizer$onTick(CallbackInfo ci) {
        if (CrystalOptimizerConfig.getInstance().fastPlace && this.crystaloptimizer$holdingCrystal()) {
            this.rightClickDelay = 0;
        }
        if (this.hitResult != null && this.hitResult.getType() == HitResult.Type.ENTITY
                && ((EntityHitResult) this.hitResult).getEntity() instanceof EndCrystal) {
            this.missTime = 0;
        }
    }
}
