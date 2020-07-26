package svenhjol.meson.iface;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonBlock {
    ItemGroup getItemGroup();

    boolean enabled();

    default int getMaxStackSize() {
        return 64;
    }

    default void register(MesonModule module, String name) {
        RegistryHandler.register((Block) this, new ResourceLocation(module.mod.getId(), name));
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
