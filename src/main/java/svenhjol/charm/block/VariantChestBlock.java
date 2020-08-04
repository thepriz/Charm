package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockReader;
import svenhjol.charm.module.VariantChests;
import svenhjol.charm.tileentity.VariantChestTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

import javax.annotation.Nullable;

public class VariantChestBlock extends ChestBlock implements IMesonBlock {
    private MesonModule module;
    private IStorageMaterial type;

    public VariantChestBlock(MesonModule module, IStorageMaterial type) {
        super(Properties.from(Blocks.CHEST), () -> VariantChests.TILE);

        this.module = module;
        this.type = type;

        this.register(module, type.getLowercaseName() + "_chest");
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new VariantChestTileEntity(module.getMod(), type);
    }
}
