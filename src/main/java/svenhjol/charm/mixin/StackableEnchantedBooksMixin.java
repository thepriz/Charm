package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import svenhjol.charm.module.StackableEnchantedBooks;
import svenhjol.meson.Meson;

import javax.annotation.Nullable;

@Mixin(RepairContainer.class)
public abstract class StackableEnchantedBooksMixin extends AbstractRepairContainer {
    public StackableEnchantedBooksMixin(@Nullable ContainerType<?> container, int i, PlayerInventory inv, IWorldPosCallable posCallable) {
        super(container, i, inv, posCallable);
    }

    @Redirect(
        method = "func_230301_a_",
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;shrink(I)V"
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/IInventory;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V",
            opcode = Opcodes.INVOKEINTERFACE,
            ordinal = 2
        )
    )
    private void anvilUpdateHook(IInventory inv, int index, ItemStack stack) {
        if (Meson.enabled("charm:stackable_enchanted_books"))
            stack = StackableEnchantedBooks.getReducedStack(inv.getStackInSlot(index));

        inv.setInventorySlotContents(index, stack);
    }
}
