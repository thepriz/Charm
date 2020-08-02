package svenhjol.charm.module;

import svenhjol.charm.block.CandleBlock;
import svenhjol.charm.item.BeeswaxItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class Candles extends MesonModule {
    public static CandleBlock CANDLE;
    public static BeeswaxItem BEESWAX;

    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Candles have a lower ambient light than torches and are made from beeswax.")
    public Candles() {}

    @Override
    public boolean test() {
        return !ModHelper.present("quark") || override;
    }

    @Override
    public void init() {
        CANDLE = new CandleBlock(this);
        BEESWAX = new BeeswaxItem(this);
    }
}
