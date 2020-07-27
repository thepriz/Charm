package svenhjol.charm.module;

import svenhjol.charm.block.RedstoneSandBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class RedstoneSand extends MesonModule {
    public static RedstoneSandBlock BLOCK;

    @Module(description = "A block that acts like sand but is powered like a block of redstone.")
    public RedstoneSand() {}

    @Override
    public void init() {
        BLOCK = new RedstoneSandBlock(this);
    }
}
