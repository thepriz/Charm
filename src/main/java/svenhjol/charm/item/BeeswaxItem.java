package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.meson.MesonModule;
import svenhjol.meson.item.MesonItem;

public class BeeswaxItem extends MesonItem {
    public BeeswaxItem(MesonModule module) {
        super(module, "beeswax", new Item.Properties().group(ItemGroup.MATERIALS));
    }
}
