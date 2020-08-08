package svenhjol.charm.block;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.helper.ModHelper;

public class QuarkBookcaseBlock extends BookcaseBlock {
    public QuarkBookcaseBlock(MesonModule module, IStorageMaterial type) {
        super(module, type, "quark_" + type.getString() + "_bookcase");
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (enabled() && ModHelper.present("quark"))
            super.fillItemGroup(group, items);
    }
}
