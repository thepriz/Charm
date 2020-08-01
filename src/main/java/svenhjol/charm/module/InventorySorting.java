package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import svenhjol.charm.client.InventorySortingClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySorting extends MesonModule {
    public static InventorySortingClient client;

    @Module(description = "LOLOL", hasSubscriptions = true)
    public InventorySorting() {}

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
                sort(wrapper, 9, 36);
                break;
            } else if (type == TILE) {
                InvWrapper wrapper = new InvWrapper(inventory);
                sort(wrapper, 0, wrapper.getSlots());
                break;
            }
        }
    }

    private static void sort(IItemHandler inventory, int startSlot, int endSlot) {
        List<ItemStack> stacks = new ArrayList<>();

        populate(inventory, stacks, startSlot, endSlot);
        mergeInventory(stacks);
        setInventory(inventory, stacks, startSlot, endSlot);
    }

    private static void populate(IItemHandler inventory, List<ItemStack> stacks, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);

            if (!stackInSlot.isEmpty())
                stacks.add(stackInSlot.copy());
        }
    }

    private static void mergeInventory(List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (stack.isEmpty())
                continue;

            for (int j = 0; j < stacks.size(); j++) {
                if (i == j)
                    continue;

                ItemStack stack1 = stacks.get(j);
                if (stack1.isEmpty())
                    continue;

                if (stack1.getCount() < stack1.getMaxStackSize()
                    && ItemStack.areItemsEqual(stack, stack1)
                    && ItemStack.areItemStackTagsEqual(stack, stack1)
                ) {
                    int setSize = stack1.getCount() + stack.getCount();
                    int carryover = Math.max(0, setSize - stack1.getMaxStackSize());
                    stack1.setCount(carryover);
                    stack.setCount(setSize - carryover);

                    if (stack.getCount() == stack.getMaxStackSize())
                        break;
                }
            }

            stacks.set(i, stack);
        }
    }

    private static boolean setInventory(IItemHandler inventory, List<ItemStack> stacks, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            int j = i - startSlot;
            ItemStack stack = j >= stacks.size() ? ItemStack.EMPTY : stacks.get(j);

            inventory.extractItem(i, inventory.getSlotLimit(i), false);
            if (!stack.isEmpty())
                if (!inventory.insertItem(i, stack, false).isEmpty())
                    return false;
        }

        return true;
    }
}
