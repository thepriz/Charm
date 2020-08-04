package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantChestBlock;
import svenhjol.charm.block.VariantTrappedChestBlock;
import svenhjol.charm.render.VariantChestTileEntityRenderer;
import svenhjol.charm.tileentity.VariantChestTileEntity;
import svenhjol.charm.tileentity.VariantTrappedChestTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

public class VariantChests extends MesonModule {
    public static final ResourceLocation NORMAL_ID = new ResourceLocation("variant_chest");
    public static final ResourceLocation TRAPPED_ID = new ResourceLocation(Charm.MOD_ID, "trapped_chest");

    public static final Map<IStorageMaterial, VariantChestBlock> NORMAL_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IStorageMaterial, VariantTrappedChestBlock> TRAPPED_CHEST_BLOCKS = new HashMap<>();

    public static TileEntityType<VariantChestTileEntity> NORMAL_TILE;
    public static TileEntityType<VariantTrappedChestTileEntity> TRAPPED_TILE;

    @Module(description = "ARGH", hasSubscriptions = true)
    public VariantChests() {}

    @Override
    public void init() {
        VanillaStorageMaterial type = VanillaStorageMaterial.SPRUCE;
        NORMAL_CHEST_BLOCKS.put(type, new VariantChestBlock(this, type));
        TRAPPED_CHEST_BLOCKS.put(type, new VariantTrappedChestBlock(this, type));

        NORMAL_TILE = TileEntityType.Builder.create(VariantChestTileEntity::new, NORMAL_CHEST_BLOCKS.values().toArray(new Block[0])).build(null);
        TRAPPED_TILE = TileEntityType.Builder.create(VariantTrappedChestTileEntity::new, TRAPPED_CHEST_BLOCKS.values().toArray(new Block[0])).build(null);

        mod.register(NORMAL_TILE, NORMAL_ID);
        mod.register(TRAPPED_TILE, TRAPPED_ID);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(NORMAL_TILE, VariantChestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TRAPPED_TILE, VariantChestTileEntityRenderer::new);
    }

    @Override
    public void onTextureStitch(TextureStitchEvent event) {
        if (event instanceof TextureStitchEvent.Pre && event.getMap().getTextureLocation().toString().equals("minecraft:textures/atlas/chest.png")) {
            TextureStitchEvent.Pre ev = (TextureStitchEvent.Pre)event;
            VariantChests.NORMAL_CHEST_BLOCKS.keySet().forEach(type -> {
                addChestTexture(ev, type, ChestType.LEFT);
                addChestTexture(ev, type, ChestType.RIGHT);
                addChestTexture(ev, type, ChestType.SINGLE);
            });
        }
    }

    private void addChestTexture(TextureStitchEvent.Pre event, IStorageMaterial variant, ChestType chestType) {
        String chestTypeName = chestType != ChestType.SINGLE ? "_" + chestType.getString().toLowerCase() : "";
        String[] bases = {"trapped", "normal"};

        for (String base : bases) {
            ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "entity/chest/" + variant.getString() + "_" + base + chestTypeName);
            VariantChestTileEntityRenderer.addTexture(variant, chestType, res, base.equals("trapped"));
            event.addSprite(res);
        }
    }
}
