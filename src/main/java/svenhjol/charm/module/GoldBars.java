package svenhjol.charm.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class GoldBars extends MesonModule {
    public static GoldBarsBlock GOLD_BARS;

    @Module(description = "Gold variant of iron bars.")
    public GoldBars() {}

    @Override
    public void init() {
        GOLD_BARS = new GoldBarsBlock(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(GOLD_BARS, RenderType.getCutoutMipped());
    }
}
