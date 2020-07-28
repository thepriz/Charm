package svenhjol.meson.iface;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;

public interface IMesonItem {
    boolean isEnabled();

    default void register(MesonModule module, String name) {
        MesonMod mod = module.getMod();
        mod.register((Item) this, new ResourceLocation(mod.getId(), name));
    }
}
