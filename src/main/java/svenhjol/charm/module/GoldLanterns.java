package svenhjol.charm.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.block.GoldLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class GoldLanterns extends MesonModule {
    public static GoldLanternBlock GOLD_LANTERN;
    public static GoldLanternBlock GOLD_SOUL_LANTERN;

    @Module(description = "Golden versions of the vanilla lanterns.")
    public GoldLanterns() {}

    @Override
    public void init() {
        GOLD_LANTERN = new GoldLanternBlock(this, "gold_lantern");
        GOLD_SOUL_LANTERN = new GoldLanternBlock(this, "gold_soul_lantern");
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(GoldLanterns.GOLD_LANTERN, RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(GoldLanterns.GOLD_SOUL_LANTERN, RenderType.getCutoutMipped());
    }
}
