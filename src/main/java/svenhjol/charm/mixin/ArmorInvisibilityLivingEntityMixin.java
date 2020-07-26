package svenhjol.charm.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.ArmorInvisibility;
import svenhjol.meson.Meson;

@Mixin(LivingEntity.class)
public abstract class ArmorInvisibilityLivingEntityMixin {
    @Shadow public abstract Iterable<ItemStack> getArmorInventoryList();

    @Inject(
        method = "getArmorCoverPercentage",
        at = @At(value = "RETURN"),
        cancellable = true
    )
    private void armorCoverHook(CallbackInfoReturnable<Float> cir) {
        if (Meson.enabled("charm:armor_invisibility")) {
            LivingEntity entity = (LivingEntity) (Object) this;
            Iterable<ItemStack> armorInventoryList = this.getArmorInventoryList();

            int i = 0;
            int j = 0;

            for (ItemStack itemstack : armorInventoryList) {
                if (!ArmorInvisibility.isArmorInvisible(entity, itemstack)) {
                    ++j;
                }
                ++i;
            }

            cir.setReturnValue(i > 0 ? (float)j / (float)i : 0.0F);
        }
    }
}
