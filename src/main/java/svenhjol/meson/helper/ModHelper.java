package svenhjol.meson.helper;

import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ModHelper {
    private static List<String> enabled = new ArrayList<>();

    public static boolean present(String... mods) {
        // basic cache
        for (String mod : mods) {
            if (enabled.contains(mod))
                return true;
        }

        ModList modList = ModList.get();

        for (String mod : mods) {
            if (modList.isLoaded(mod)) {
                enabled.add(mod);
            } else {
                return false;
            }
        }

        return true;
    }
}
