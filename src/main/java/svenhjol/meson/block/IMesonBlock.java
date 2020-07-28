package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;

public interface IMesonBlock {
    ItemGroup getItemGroup();

    boolean enabled();

    default int getMaxStackSize() {
        return 64;
    }

    default void register(MesonModule module, String name) {
        module.getMod().register((Block)this, new ResourceLocation(module.getMod().getId(), name));
    }

    default BlockItem getBlockItem() {
        Item.Properties props = new Item.Properties();

        if (enabled()) {
            ItemGroup group = getItemGroup();
            if (group != null) props.group(group);
            props.maxStackSize(getMaxStackSize());
        }

        return new BlockItem((Block)this, props);
    }
}
