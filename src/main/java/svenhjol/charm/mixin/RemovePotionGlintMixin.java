package svenhjol.charm.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.meson.Meson;

@Mixin(PotionItem.class)
public class RemovePotionGlintMixin {
    @Inject(
        method = "hasEffect",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hasEffectHook(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Meson.enabled("charm:remove_potion_glint"))
            cir.setReturnValue(false);
    }
}
