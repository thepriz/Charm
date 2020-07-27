package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockReader;
import svenhjol.charm.tileentity.VariantBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IStorageMaterial;

import javax.annotation.Nullable;

public class VariantBarrelBlock extends BarrelBlock implements IMesonBlock {
    protected MesonModule module;
    protected IStorageMaterial type;

    public VariantBarrelBlock(MesonModule module, IStorageMaterial type) {
        super(AbstractBlock.Properties.from(Blocks.BARREL));

        this.module = module;
        this.type = type;

        this.register(module, type.getLowercaseName() + "_barrel");
        this.setDefaultState(this.getStateContainer()
            .getBaseState()
            .with(PROPERTY_FACING, Direction.NORTH)
            .with(PROPERTY_OPEN, false)
        );
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
        return new VariantBarrelTileEntity(module.getMod(), type);
    }
}
