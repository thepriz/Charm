package svenhjol.charm.modules;

import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.OverrideHandler;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class StackableBooks extends MesonModule {
    @Config(name = "Stack size", description = "Maximum enchanted book stack size.")
    public static int stackSize = 16;

    @Module(description = "Allows enchanted books to stack.", hasSubscriptions = true)
    public StackableBooks() {}

    @Override
    public void init() {
        if (enabled) {
            EnchantedBookItem enchantedBookItem = new EnchantedBookItem((new Item.Properties()).maxStackSize(stackSize).rarity(Rarity.UNCOMMON));
            OverrideHandler.changeVanillaItem(enchantedBookItem, new ResourceLocation("enchanted_book"));
        }
    }

    public static ItemStack checkItemStack(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
            stack.shrink(1);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
