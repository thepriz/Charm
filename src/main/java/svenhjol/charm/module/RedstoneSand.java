package svenhjol.charm.module;

import svenhjol.charm.block.RedstoneSandBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class RedstoneSand extends MesonModule {
    public static RedstoneSandBlock REDSTONE_SAND;

    @Module(description = "A block that acts like sand but is powered like a block of redstone.")
    public RedstoneSand() {}

    @Override
    public void init() {
        REDSTONE_SAND = new RedstoneSandBlock(this);
    }
}
