package svenhjol.charm.module;

import svenhjol.charm.iface.IQuarkCompat;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Module;

public class Quark extends MesonModule {
    public static IQuarkCompat compat;

    @Module(description = "Quark integration.", alwaysEnabled = true)
    public Quark() {}

    @Override
    public void init() {
        try {
            if (ModHelper.present("quark")) {
//                compat = QuarkCompat.class.getDeclaredConstructor().newInstance();
                Meson.LOG.debug("Loaded Quark compatibility class");
            }
        } catch (Exception e) {
            Meson.LOG.error("Failed to load Quark compatibility class: " + e.getMessage());
        }
    }
}
