package svenhjol.charm.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.block.RedstoneLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class RedstoneLanterns extends MesonModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Module(description = "A lantern that emits light when a redstone signal is received.")
    public RedstoneLanterns() {}

    @Override
    public void init() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(REDSTONE_LANTERN, RenderType.getCutout());
    }
}
