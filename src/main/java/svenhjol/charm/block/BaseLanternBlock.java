package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;

public abstract class BaseLanternBlock extends ImprovedLanternBlock implements IMesonBlock {
    protected MesonModule module;

    public BaseLanternBlock(MesonModule module, String name, Block.Properties props) {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (enabled()) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}