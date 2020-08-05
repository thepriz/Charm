package svenhjol.charm.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class GoldBars extends MesonModule {
    public static GoldBarsBlock GOLD_BARS;

    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Gold variant of vanilla iron bars.")
    public GoldBars() {}

    @Override
    public void init() {
        GOLD_BARS = new GoldBarsBlock(this);
    }

    @Override
    public boolean test() {
        return !ModHelper.present("quark") || override;
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(GOLD_BARS, RenderType.getCutoutMipped());
    }
}
