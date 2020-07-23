package svenhjol.meson.helper;

import net.minecraftforge.fml.ModList;

public class ModHelper {
    public static boolean present(String... mods) {
        boolean loaded = false;
        ModList modList = ModList.get();

        for (String mod : mods) {
            loaded = loaded || modList.isLoaded(mod);
        }

        return loaded;
    }
}
