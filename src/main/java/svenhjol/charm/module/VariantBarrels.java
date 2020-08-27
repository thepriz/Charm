package svenhjol.charm.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantBarrelBlock;
import svenhjol.charm.tileentity.VariantBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.handler.OverrideHandler;
import svenhjol.meson.iface.Module;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class VariantBarrels extends MesonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "barrel");
    public static final Map<IStorageMaterial, VariantBarrelBlock> BARREL_BLOCKS = new HashMap<>();

    public static TileEntityType<VariantBarrelTileEntity> TILE;

    @Module(description = "Barrels available in all types of vanilla wood.")
    public VariantBarrels() {}

    @Override
    public void init() {
        for (VanillaStorageMaterial type : VanillaStorageMaterial.values()) {
            BARREL_BLOCKS.put(type, new VariantBarrelBlock(this, type));
        }

        TILE = TileEntityType.Builder.create(VariantBarrelTileEntity::new).build(null);
        mod.register(TILE, ID);

        if (enabled) {
            OverrideHandler.changeBlockTranslationKey(Blocks.BARREL, "block.charm.fisherman_barrel");
            OverrideHandler.changeItemTranslationKey(Items.BARREL, "item.charm.fisherman_barrel");
        }
    }
}
