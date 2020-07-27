package svenhjol.charm.module;

import svenhjol.charm.block.SugarBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class BlockOfSugar extends MesonModule {
    public static SugarBlock BLOCK;

    @Module(description = "A storage block for sugar. It obeys gravity and dissolves in water.")
    public BlockOfSugar() {}

    @Override
    public void init() {
        BLOCK = new SugarBlock(this);
    }
}
