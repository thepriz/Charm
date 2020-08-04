package svenhjol.charm.tileentity;

import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import svenhjol.charm.block.IVariantChestBlock;
import svenhjol.charm.module.VariantChests;
import svenhjol.meson.enums.IStorageMaterial;

import javax.annotation.Nullable;

public class VariantChestTileEntity extends ChestTileEntity {
    private IStorageMaterial materialType = null;

    public VariantChestTileEntity() {
        super(VariantChests.NORMAL_TILE);
    }

    protected VariantChestTileEntity(TileEntityType<?> tile) {
        super(tile);
    }

    @Nullable
    public IStorageMaterial getMaterialType() {
        if (materialType == null && world != null)
            return ((IVariantChestBlock)this.getBlockState().getBlock()).getMaterialType();

        return materialType;
    }

    public void setMaterialType(IStorageMaterial materialType) {
        this.materialType = materialType;
    }
}
