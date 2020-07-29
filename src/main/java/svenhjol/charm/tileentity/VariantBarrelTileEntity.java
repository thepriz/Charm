package svenhjol.charm.tileentity;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import svenhjol.charm.Charm;
import svenhjol.charm.module.VariantBarrels;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.mixin.BarrelTileEntityAccessor;

public class VariantBarrelTileEntity extends BarrelTileEntity {
    protected IStorageMaterial type;
    protected MesonMod mod;

    public VariantBarrelTileEntity() {
        this(Meson.getMod(Charm.MOD_ID), VanillaStorageMaterial.OAK);
    }

    public VariantBarrelTileEntity(MesonMod mod, IStorageMaterial type) {
        BarrelTileEntityAccessor.invokeConstructor(VariantBarrels.TILE);

        this.mod = mod;
        this.type = type;

        this.setCustomName(new TranslationTextComponent("block." + mod.getId() + "." + type.getLowercaseName() + "_barrel"));
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

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block." + this.mod.getId() + "." + this.type.getLowercaseName() + "_barrel");
    }
}
