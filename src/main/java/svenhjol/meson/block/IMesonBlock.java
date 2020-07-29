package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.mixin.FireBlockAccessor;

public interface IMesonBlock {
    ItemGroup getItemGroup();

    boolean enabled();

    default int getMaxStackSize() {
        return 64;
    }

    default int getBurnTime() { return 300; }

    default void register(MesonModule module, String name) {
        module.getMod().register((Block)this, new ResourceLocation(module.getMod().getId(), name));
    }

    default BlockItem getBlockItem() {
        Item.Properties props = new Item.Properties();

        if (enabled()) {
            // set up custom props for the blockitem
            ItemGroup group = getItemGroup();
            if (group != null) props.group(group);
            props.maxStackSize(getMaxStackSize());

            MesonBlockItem blockItem = new MesonBlockItem((Block)this, props);

            // set attributes on the blockitem instance
            blockItem.setBurnTime(getBurnTime());

            return blockItem;
        }

        return new BlockItem((Block)this, props);
    }

    default void setFireInfo(int encouragement, int flammability) {
        ((FireBlockAccessor) Blocks.FIRE).callSetFireInfo((Block)this, encouragement, flammability);
    }
}
