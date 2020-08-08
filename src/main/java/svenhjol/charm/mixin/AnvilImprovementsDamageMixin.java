package svenhjol.charm.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.AnvilImprovements;
import svenhjol.meson.Meson;

import java.util.Random;

@Mixin(AnvilBlock.class)
public class AnvilImprovementsDamageMixin {
    @Inject(
        method = "damage",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onDamageHook(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (Meson.enabled("charm:anvil_improvements") && AnvilImprovements.strongerAnvils)
            if (new Random().nextFloat() < 0.5F)
                cir.setReturnValue(state);
    }
}
