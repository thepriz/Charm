package svenhjol.charm.decoration.module;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.GoldChainBlock;
import svenhjol.charm.decoration.client.GoldChainsClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "Gold version of the vanilla chain.")
public class GoldChains extends MesonModule {
    public static GoldChainBlock block;
    public static GoldChainsClient client;

    @Override
    public void init() {
        block = new GoldChainBlock(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        client = new GoldChainsClient();
    }
}
