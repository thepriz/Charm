package svenhjol.charm.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.block.GoldChainBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class GoldChains extends MesonModule {
    public static GoldChainBlock GOLD_CHAIN;

    @Module(description = "Gold version of the vanilla chain.")
    public GoldChains() {}

    @Override
    public void init() {
        GOLD_CHAIN = new GoldChainBlock(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(GOLD_CHAIN, RenderType.getCutoutMipped());
    }
}
