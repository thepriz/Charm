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
import svenhjol.charm.render.VariantChestTileEntityRenderer;
import svenhjol.charm.tileentity.VariantChestTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

public class VariantChests extends MesonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "chest");
    public static final Map<IStorageMaterial, VariantChestBlock> CHEST_BLOCKS = new HashMap<>();
    public static TileEntityType<VariantChestTileEntity> TILE;

    @Module(description = "ARGH", hasSubscriptions = true)
    public VariantChests() {}

    @Override
    public void init() {
        for (VanillaStorageMaterial type : VanillaStorageMaterial.values()) {
            CHEST_BLOCKS.put(type, new VariantChestBlock(this, type));
        }

        TILE = TileEntityType.Builder.create(VariantChestTileEntity::new, CHEST_BLOCKS.values().toArray(new Block[0])).build(null);
        mod.register(TILE, ID);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(TILE, VariantChestTileEntityRenderer::new);
    }

    @Override
    public void onTextureStitch(TextureStitchEvent event) {
        if (event instanceof TextureStitchEvent.Pre
            && event.getMap().getTextureLocation().toString().equals("minecraft:textures/atlas/chest.png")
        ) {
            TextureStitchEvent.Pre ev = (TextureStitchEvent.Pre)event;
            VariantChests.CHEST_BLOCKS.keySet().forEach(type -> {
                // add normal chest
                addChestTexture(ev, type, ChestType.LEFT, new ResourceLocation(Charm.MOD_ID, "entity/chest/" + type.getString() + "_normal_left"));
                addChestTexture(ev, type, ChestType.RIGHT, new ResourceLocation(Charm.MOD_ID, "entity/chest/" + type.getString() + "_normal_right"));
                addChestTexture(ev, type, ChestType.SINGLE, new ResourceLocation(Charm.MOD_ID, "entity/chest/" + type.getString() + "_normal"));

                // add trapped chest
                addChestTexture(ev, type, ChestType.LEFT, new ResourceLocation(Charm.MOD_ID, "entity/chest/" + type.getString() + "_trapped_left"));
                addChestTexture(ev, type, ChestType.RIGHT, new ResourceLocation(Charm.MOD_ID, "entity/chest/" + type.getString() + "_trapped_right"));
                addChestTexture(ev, type, ChestType.SINGLE, new ResourceLocation(Charm.MOD_ID, "entity/chest/" + type.getString() + "_trapped"));
            });
        }
    }

    private void addChestTexture(TextureStitchEvent.Pre event, IStorageMaterial materialType, ChestType chestType, ResourceLocation res) {
        event.addSprite(res);
        VariantChestTileEntityRenderer.addMaterial(materialType, chestType, res);
    }
}
