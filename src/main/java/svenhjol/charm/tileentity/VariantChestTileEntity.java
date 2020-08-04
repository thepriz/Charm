package svenhjol.charm.tileentity;

import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.text.TranslationTextComponent;
import svenhjol.charm.Charm;
import svenhjol.charm.module.VariantChests;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;

public class VariantChestTileEntity extends ChestTileEntity {
    protected IStorageMaterial type;
    protected MesonMod mod;

    public VariantChestTileEntity() {
        this(Meson.getMod(Charm.MOD_ID), VanillaStorageMaterial.OAK);
    }

    public VariantChestTileEntity(MesonMod mod, IStorageMaterial type) {
        super(VariantChests.TILE);

        this.mod = mod;
        this.type = type;

        this.setCustomName(new TranslationTextComponent("block." + mod.getId() + "." + type.getLowercaseName() + "_chest"));
    }

    public IStorageMaterial getStorageMaterialType() {
        return this.type;
    }
}
