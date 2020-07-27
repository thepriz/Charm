package svenhjol.charm.module;

import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class AnvilImprovements extends MesonModule {
    @Config(name = "Remove Too Expensive", description = "If true, removes the maximum cost of 40 XP when working items on the anvil.")
    public static boolean removeTooExpensive = true;

    @Module(description = "Removes minimum cost and maximum XP costs on the anvil.")
    public AnvilImprovements() { }
}
