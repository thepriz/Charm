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
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.List;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySorting extends MesonModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    public static InventorySortingClient client;

    @Module(description = "Button to automatically tidy inventories.", hasSubscriptions = true)
    public InventorySorting() {
        InventorySortingHandler.init();
    }

    @Override
    public boolean shouldSetup() {
        return !ModHelper.present("quark") || override;
    }

    @Override
    public void initClient() {
        client = new InventorySortingClient(this);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static void serverCallback(ServerPlayerEntity player, int type) {
        Container useContainer;

        if (player.isSpectator())
            return;

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
