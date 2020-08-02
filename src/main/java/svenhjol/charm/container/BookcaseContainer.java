package svenhjol.charm.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import svenhjol.charm.module.Bookcases;
import svenhjol.charm.tileentity.BookcaseTileEntity;
import svenhjol.meson.container.MesonContainer;

public class BookcaseContainer extends MesonContainer {
    private BookcaseContainer(ContainerType<?> type, int id, PlayerInventory player, IInventory inventory) {
        super(type, id, player, inventory);

        int index = 0;

        // container's inventory slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new BookcaseSlot(inventory, index++, 8 + (i * 18), 18));
        }

        index = 9;

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, index++, 8 + c * 18, 50 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + (i * 18), 108));
        }
    }

    public static BookcaseContainer instance(int id, PlayerInventory playerInventory, IInventory inventory) {
        return new BookcaseContainer(Bookcases.CONTAINER, id, playerInventory, inventory);
    }

    public static BookcaseContainer instance(int id, PlayerInventory playerInventory) {
        return instance(id, playerInventory, new Inventory(BookcaseTileEntity.SIZE));
    }
}
