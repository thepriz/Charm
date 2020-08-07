package svenhjol.charm.tileentity;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.SoundEvents;
import svenhjol.charm.mixin.accessor.BarrelTileEntityAccessor;
import svenhjol.charm.module.VariantBarrels;
import svenhjol.meson.MesonMod;
import svenhjol.meson.enums.IStorageMaterial;

public class VariantBarrelTileEntity extends BarrelTileEntity {
    protected IStorageMaterial type;
    protected MesonMod mod;

    public VariantBarrelTileEntity() {
        BarrelTileEntityAccessor.invokeConstructor(VariantBarrels.TILE);
    }

    /**
     * We need to override this method to work around the strict block check.
     */
    @Override
    public void barrelTick() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        if (this.world == null)
            return;

        ((BarrelTileEntityAccessor)this).setNumPlayersUsing(ChestTileEntity.calculatePlayersUsing(this.world, this, i, j, k));
        if (((BarrelTileEntityAccessor)this).getNumPlayersUsing() > 0) {
            ((BarrelTileEntityAccessor)this).callScheduleTick();
        } else {
            BlockState blockstate = this.getBlockState();
            if (!(blockstate.getBlock() instanceof BarrelBlock)) {
                this.remove();
                return;
            }

            boolean flag = blockstate.get(BarrelBlock.PROPERTY_OPEN);
            if (flag) {
                ((BarrelTileEntityAccessor)this).callPlaySound(blockstate, SoundEvents.BLOCK_BARREL_CLOSE);
                ((BarrelTileEntityAccessor)this).callSetOpenProperty(blockstate, false);
            }
        }
    }
}
