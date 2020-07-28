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

public class VariantBarrelTileEntity extends BarrelTileEntity {
    protected IStorageMaterial type;
    protected MesonMod mod;

    public VariantBarrelTileEntity() {
        this(Meson.getMod(Charm.MOD_ID), VanillaStorageMaterial.OAK);
    }

    public VariantBarrelTileEntity(MesonMod mod, IStorageMaterial type) {
        super(VariantBarrels.TILE);

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

        this.numPlayersUsing = ChestTileEntity.calculatePlayersUsing(this.world, this, i, j, k);
        if (this.numPlayersUsing > 0) {
            this.scheduleTick();
        } else {
            BlockState blockstate = this.getBlockState();
            if (!(blockstate.getBlock() instanceof BarrelBlock)) {
                this.remove();
                return;
            }

            boolean flag = blockstate.get(BarrelBlock.PROPERTY_OPEN);
            if (flag) {
                this.playSound(blockstate, SoundEvents.BLOCK_BARREL_CLOSE);
                this.setOpenProperty(blockstate, false);
            }
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block." + this.mod.getId() + "." + this.type.getLowercaseName() + "_barrel");
    }
}
