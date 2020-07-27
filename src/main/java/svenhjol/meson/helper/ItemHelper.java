package svenhjol.meson.helper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemHelper {
    public static Class<? extends Block> getBlockClass(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()).getClass();
    }
}
