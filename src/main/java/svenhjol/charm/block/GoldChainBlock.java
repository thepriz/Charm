package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChainBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;

@SuppressWarnings("NullableProblems")
public class GoldChainBlock extends ChainBlock implements IMesonBlock {
    private final MesonModule module;

    public GoldChainBlock(MesonModule module) {
        super(AbstractBlock.Properties.from(Blocks.CHAIN));
        this.module = module;
        this.register(module, "gold_chain");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemGroup(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
