package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class MesonBlockItem extends BlockItem {
    private int burnTime;

    public MesonBlockItem(IMesonBlock blockIn, Properties props) {
        super((Block)blockIn, props);

        // set blockitem's registryname same as block's
        Block b = (Block)blockIn;
        if (b.getRegistryName() != null)
            this.setRegistryName(b.getRegistryName());

        // set other props
        this.setBurnTime(blockIn.getBurnTime());
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return this.burnTime;
    }
}
