package svenhjol.charm.module;

import svenhjol.charm.block.GunpowderBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class BlockOfGunpowder extends MesonModule {
    public static GunpowderBlock BLOCK;

    @Module(description = "A storage block for gunpowder. It obeys gravity and dissolves in lava.")
    public BlockOfGunpowder() {}

    @Override
    public void init() {
        BLOCK = new GunpowderBlock(this);
    }
}
