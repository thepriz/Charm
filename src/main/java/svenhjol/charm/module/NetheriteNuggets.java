package svenhjol.charm.module;

import svenhjol.charm.item.NetheriteNuggetItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class NetheriteNuggets extends MesonModule {
    public static NetheriteNuggetItem NETHERITE_NUGGET;

    @Module(description = "Add netherite nuggets that can be combined to create a netherite ingot.")
    public NetheriteNuggets() {}

    @Override
    public void init() {
        NETHERITE_NUGGET = new NetheriteNuggetItem(this);
    }
}
