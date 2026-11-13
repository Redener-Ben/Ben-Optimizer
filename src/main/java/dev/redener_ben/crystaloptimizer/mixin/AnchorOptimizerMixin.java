package dev.redener_ben.crystaloptimizer.mixin;

import dev.redener_ben.crystaloptimizer.client.config.CrystalOptimizerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Anchor Optimizer: Wenn du einen geladenen Respawn Anchor ausserhalb des Nethers
 * zur Explosion bringst, wird er sofort auf deinem Client entfernt. Du musst nicht
 * auf das Block-Update vom Server warten und kannst direkt den naechsten Anchor setzen.
 *
 * Laden mit Glowstone wird NICHT veraendert (kein Glowstone Fast Place).
 */
@Mixin(MultiPlayerGameMode.class)
public class AnchorOptimizerMixin {
    @Unique private BlockPos crystaloptimizer$anchorPos;

    @Inject(method = "useItemOn", at = @At("HEAD"))
    private void crystaloptimizer$beforeUse(LocalPlayer player, InteractionHand hand, BlockHitResult hit,
                                             CallbackInfoReturnable<InteractionResult> cir) {
        this.crystaloptimizer$anchorPos = null;
        if (!CrystalOptimizerConfig.getInstance().anchorOptimizer) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || level.dimension() == Level.NETHER) return; // Im Nether explodiert er nicht

        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.RESPAWN_ANCHOR)) return;

        int charge = state.getValue(RespawnAnchorBlock.CHARGE);
        if (charge <= 0) return;

        // Wenn diese Interaktion den Anchor mit Glowstone laedt, explodiert nichts.
        boolean canCharge = charge < RespawnAnchorBlock.MAX_CHARGES;
        boolean glowInHand = player.getItemInHand(hand).is(Items.GLOWSTONE);
        boolean glowInOffhand = player.getOffhandItem().is(Items.GLOWSTONE);
        if (canCharge && (glowInHand || (hand == InteractionHand.MAIN_HAND && glowInOffhand))) return;

        this.crystaloptimizer$anchorPos = pos.immutable();
    }

    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void crystaloptimizer$afterUse(LocalPlayer player, InteractionHand hand, BlockHitResult hit,
                                            CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos pos = this.crystaloptimizer$anchorPos;
        this.crystaloptimizer$anchorPos = null;
        if (pos == null || !cir.getReturnValue().consumesAction()) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.getBlockState(pos).is(Blocks.RESPAWN_ANCHOR)) return;

        // Nur auf dem Client: Der Server schickt danach sowieso Luft fuer diese Position.
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
    }
}
