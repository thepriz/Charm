package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import svenhjol.charm.tileentity.VariantBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

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

    @Override
    public int getBurnTime() {
        return 300;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        VariantBarrelTileEntity barrel = new VariantBarrelTileEntity();

        // TODO this might be a shit way to set display names, please check
        barrel.setCustomName(new TranslationTextComponent("block." + this.module.mod.getId() + "." + type.getLowercaseName() + "_barrel"));

        return barrel;
    }
}
