package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.block.ImprovedLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.OverrideHandler;
import svenhjol.meson.iface.Module;

public class LanternsObeyGravity extends MesonModule {
    @Module(description = "Vanilla lanterns obey gravity.")
    public LanternsObeyGravity() {}

    @Override
    public void init() {
        if (enabled) {
            ResourceLocation LANTERN_ID = new ResourceLocation("lantern");
            ResourceLocation SOUL_LANTERN_ID = new ResourceLocation("soul_lantern");

            Block.Properties props = Block.Properties.from(Blocks.LANTERN);

            Block lanternBlock = new ImprovedLanternBlock(props);
            Block soulLanternBlock = new ImprovedLanternBlock(props);
            Item lanternItem = new BlockItem(lanternBlock, (new Item.Properties().group(ItemGroup.DECORATIONS)));
            Item soulLanternItem = new BlockItem(soulLanternBlock, (new Item.Properties().group(ItemGroup.DECORATIONS)));

            OverrideHandler.changeVanillaBlock(lanternBlock, LANTERN_ID);
            OverrideHandler.changeVanillaItem(lanternItem, LANTERN_ID);
            OverrideHandler.changeVanillaBlock(soulLanternBlock, SOUL_LANTERN_ID);
            OverrideHandler.changeVanillaItem(soulLanternItem, SOUL_LANTERN_ID);
        }
    }
}
