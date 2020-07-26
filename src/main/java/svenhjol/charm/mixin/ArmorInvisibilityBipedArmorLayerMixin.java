package svenhjol.charm.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.ArmorInvisibility;
import svenhjol.meson.Meson;

@Mixin(BipedArmorLayer.class)
public class ArmorInvisibilityBipedArmorLayerMixin<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> {
    @Inject(
        method = "func_241739_a_",
        at = @At("HEAD"),
        cancellable = true
    )
    private void renderArmorPartHook(MatrixStack matrix, IRenderTypeBuffer buf, T entity, EquipmentSlotType slot, int i, A model, CallbackInfo ci) {
        if (Meson.enabled("charm:armor_invisibility")) {
            ItemStack stack = entity.getItemStackFromSlot(slot);
            if (ArmorInvisibility.isArmorInvisible(entity, stack))
                ci.cancel();
        }
    }
}
