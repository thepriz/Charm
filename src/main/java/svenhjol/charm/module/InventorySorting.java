package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import svenhjol.charm.client.InventorySortingClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.*;
import java.util.function.Predicate;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySorting extends MesonModule {
    public static InventorySortingClient client;
    private static final Map<Predicate<ItemStack>, Comparator<ItemStack>> testCompare = new HashMap<>();

    @Module(description = "LOLOL", hasSubscriptions = true)
    public InventorySorting() {
        testCompare.put(clazzTest(BlockItem.class).negate(), anyCompare());
        testCompare.put(clazzTest(BlockItem.class), blockCompare());
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
        stacks.sort(InventorySorting::compare); // TODO world's crappiest sorting
        setInventory(inventory, stacks, startSlot, endSlot); // TODO handle fail here
    }

    private static void populate(IItemHandler inventory, List<ItemStack> stacks, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);

            if (!stackInSlot.isEmpty())
                stacks.add(stackInSlot.copy());
        }
    }

    /**
     * Core merging code from Quark's SortingHandler.
     * @param stacks Inventory stack to merge within
     */
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

        stacks.removeIf((ItemStack stack) -> stack.isEmpty() || stack.getCount() == 0);
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

    private static int compare(ItemStack stack1, ItemStack stack2) {
        if (stack1 == stack2) {
            return 0;
        } else if (stack1.isEmpty()) {
            return -1;
        } else if (stack2.isEmpty()) {
            return 1;
        }

        int index1 = -1, index2 = -1, index = 0;

        for (Predicate<ItemStack> predicate : testCompare.keySet()) {
            if (predicate.test(stack1))
                index1 = index;
            if (predicate.test(stack2))
                index2 = index;

            if (index1 >= 0 && index1 == index2)
                return testCompare.get(predicate).compare(stack1, stack2);

            index++;
        }

        return index1 - index2;
    }

    private static Comparator<ItemStack> blockCompare() {
        return compare(Comparator.comparing(s -> Item.getIdFromItem(s.getItem())),
            (ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
            (ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
    }

    private static Comparator<ItemStack> anyCompare() {
        return compare(Comparator.comparing(s -> Item.getIdFromItem(s.getItem())),
            (ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
            (ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
    }

    private static Comparator<ItemStack> compare(Comparator<ItemStack>... comparators) {
        return ((stack1, stack2) -> {
            for (Comparator<ItemStack> comparator : comparators) {
                int res = comparator.compare(stack1, stack2);
                if (res != 0)
                    return res;
            }
            return 0;
        });
    }

    private static Predicate<ItemStack> clazzTest(Class<? extends Item> clazz) {
        return stack -> !stack.isEmpty() && clazz.isInstance(stack.getItem());
    }
}
