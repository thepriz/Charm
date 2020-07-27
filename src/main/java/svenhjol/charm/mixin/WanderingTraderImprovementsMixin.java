package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WanderingTraderSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.WanderingTraderImprovements;
import svenhjol.meson.Meson;

import java.util.Random;

@Mixin(WanderingTraderSpawner.class)
public class WanderingTraderImprovementsMixin {

    @Inject(
        method = "func_234562_a_",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/server/ServerWorld;getPointOfInterestManager()Lnet/minecraft/village/PointOfInterestManager;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void tryTraderSpawnHook(ServerWorld world, CallbackInfoReturnable<Boolean> cir, PlayerEntity playerentity) {
        if (Meson.enabled("charm:wandering_trader_improvements")
            && !WanderingTraderImprovements.isSignalFireInRange(world, playerentity.getPosition()))
            cir.setReturnValue(false);
    }

    // TODO: This is JUST for testing wandering trader spawns. Do not enable in production!
    @Redirect(
        method = "func_234562_a_",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Random;nextInt(I)I"
        )
    )
    private int randomCheckHook(Random random, int i) {
        if (false && Meson.enabled("charm:wandering_trader_improvements"))
            return 0; // Set to 10 for vanilla functionality

        return 10;
    }
}
