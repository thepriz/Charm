package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

public class MesonBlockItem extends BlockItem {
    private int burnTime;
    private final BiConsumer<ItemStack, Boolean> inventoryTickConsumer;

    public MesonBlockItem(IMesonBlock blockIn, Properties props) {
        super((Block)blockIn, props);

        // set blockitem's registryname same as block's
        Block b = (Block)blockIn;
        if (b.getRegistryName() != null)
            this.setRegistryName(b.getRegistryName());

        // set other props
        this.setBurnTime(blockIn.getBurnTime());

        // callable inventory tick consumer from the meson block
        this.inventoryTickConsumer = blockIn.getInventoryTickConsumer();
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return this.burnTime;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (inventoryTickConsumer != null)
            inventoryTickConsumer.accept(stack, isSelected);
    }
}
