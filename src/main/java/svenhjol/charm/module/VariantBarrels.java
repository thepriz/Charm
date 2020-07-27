package svenhjol.charm.module;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantBarrelBlock;
import svenhjol.charm.tileentity.VariantBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.iface.IStorageMaterial;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class VariantBarrels extends MesonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "barrel");
    public static final Map<IStorageMaterial, VariantBarrelBlock> BLOCKS = new HashMap<>();

    public static TileEntityType<VariantBarrelTileEntity> TILE;

    @Module(description = "Barrels are available in all types of vanilla wood.")
    public VariantBarrels() {}

    @Override
    public void init() {
        for (VanillaStorageMaterial type : VanillaStorageMaterial.values()) {
            BLOCKS.put(type, new VariantBarrelBlock(this, type));
        }

        TILE = TileEntityType.Builder.create(VariantBarrelTileEntity::new).build(null);
        mod.register(TILE, ID);
    }
}
