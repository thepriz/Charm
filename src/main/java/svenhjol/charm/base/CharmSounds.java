package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonMod;

import java.util.HashMap;
import java.util.Map;

public class CharmSounds {
    private static final Map<ResourceLocation, SoundEvent> REGISTER = new HashMap<>();

    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");

    public static SoundEvent createSound(String name) {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res);
        REGISTER.put(res, sound);
        return sound;
    }

    public static void init(MesonMod mod) {
        REGISTER.forEach((res, sound) -> mod.register(sound, res));
    }
}
