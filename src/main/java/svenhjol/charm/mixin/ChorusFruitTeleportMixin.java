package svenhjol.charm.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.BlockOfEnderPearls;
import svenhjol.meson.Meson;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitTeleportMixin {
    @Inject(
        method = "onItemUseFinish",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onItemUseFinishHook(ItemStack stack, World worldIn, LivingEntity entityLiving, CallbackInfoReturnable<ItemStack> cir) {
        if (Meson.enabled("charm:block_of_ender_pearls") && BlockOfEnderPearls.chorusTeleport(entityLiving, stack))
            cir.setReturnValue(stack);
    }
}
