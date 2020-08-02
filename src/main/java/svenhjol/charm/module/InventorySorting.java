package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.wrapper.InvWrapper;
import svenhjol.charm.client.InventorySortingClient;
import svenhjol.charm.handler.InventorySortingHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.List;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySorting extends MesonModule {
    public static InventorySortingClient client;

    @Module(description = "LOLOL", hasSubscriptions = true)
    public InventorySorting() {
        InventorySortingHandler.init();
    }

    @Override
    public void initClient() {
        client = new InventorySortingClient(this);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static void serverCallback(ServerPlayerEntity player, int type) {
        Container useContainer;

        if (type == PLAYER && player.container != null) {
            useContainer = player.container;
        } else if (type == TILE && player.openContainer != null) {
            useContainer = player.openContainer;
        } else {
            return;
        }

        List<Slot> slots = useContainer.inventorySlots;
        for (Slot slot : slots) {
            IInventory inventory = slot.inventory;

            if (type == PLAYER && slot.inventory == player.inventory) {
                InvWrapper wrapper = new InvWrapper(inventory);
                InventorySortingHandler.sort(wrapper, 9, 36);
                break;
            } else if (type == TILE) {
                InvWrapper wrapper = new InvWrapper(inventory);
                InventorySortingHandler.sort(wrapper, 0, wrapper.getSlots());
                break;
            }
        }
    }
}
