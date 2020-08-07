package svenhjol.charm.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.block.VariantChestBlock;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.ItemHelper;

@Mixin(AbstractChestedHorseEntity.class)
public abstract class VariantChestsMixin extends AbstractHorseEntity {
    protected VariantChestsMixin(EntityType<? extends AbstractHorseEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Shadow protected abstract void playChestEquipSound();

    @Shadow public abstract boolean hasChest();

    @Shadow public abstract void setChested(boolean chested);

    @Inject(
        method = "func_230254_b_",
        at = @At("RETURN"),
        cancellable = true
    )
    private void checkVariantChestHook(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
        if (Meson.enabled("charm:variant_chests")) {
            ItemStack held = player.getHeldItem(hand);

            if (!this.hasChest() && ItemHelper.getBlockClass(held) == VariantChestBlock.class) {
                this.setChested(true);
                this.playChestEquipSound();
                if (!player.abilities.isCreativeMode)
                    held.shrink(1);

                this.initHorseChest();
                cir.setReturnValue(ActionResultType.func_233537_a_(this.world.isRemote));
            }
        }
    }
}
