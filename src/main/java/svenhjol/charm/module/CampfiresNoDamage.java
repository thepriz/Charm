package svenhjol.charm.module;

import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class CampfiresNoDamage extends MesonModule {
    @Config(name = "Soul campfires cause damage", description = "If true, soul campfires will still cause damage to mobs.")
    public static boolean soulCampfiresDamage = true;

    @Module(description = "Campfires no longer damage mobs.")
    public CampfiresNoDamage() {}
}
