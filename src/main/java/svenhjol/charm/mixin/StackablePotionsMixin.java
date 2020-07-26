package svenhjol.charm.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.meson.Meson;

@Mixin(BrewingRecipeRegistry.class)
public class StackablePotionsMixin {
    @Inject(
        method = "isValidInput(Lnet/minecraft/item/ItemStack;)Z",
        cancellable = true,
        remap = false,
        at = @At(value = "RETURN", ordinal = 0)
    )
    private static void isValidInputHook(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Meson.enabled("charm:stackable_potions") && !cir.getReturnValue())
            cir.setReturnValue(true);
    }
}
