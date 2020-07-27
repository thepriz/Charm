package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.util.IntReferenceHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.AnvilImprovements;
import svenhjol.meson.Meson;

@Mixin(RepairContainer.class)
public class AnvilImprovementsContainerMixin {
    @Shadow @Final private IntReferenceHolder maximumCost;

    @Redirect(
        method = "updateRepairOutput",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;isCreativeMode:Z",
            ordinal = 1
        )
    )
    private boolean maximumCostCheckHook(PlayerAbilities abilities) {
        if (Meson.enabled("charm:anvil_improvements") && AnvilImprovements.removeTooExpensive)
            return true;

        return abilities.isCreativeMode;
    }

    @Inject(
        method = "func_230303_b_",
        at = @At("HEAD"),
        cancellable = true
    )
    private void minimumCostCheckHook(PlayerEntity player, boolean unused, CallbackInfoReturnable<Boolean> cir) {
        if (Meson.enabled("charm:anvil_improvements"))
            cir.setReturnValue( (player.abilities.isCreativeMode || player.experienceLevel >= this.maximumCost.get()) && this.maximumCost.get() > -1 );
    }
}
