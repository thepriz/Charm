package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonMod;

import java.util.ArrayList;
import java.util.List;

public class CharmSounds {
    private static final List<SoundEvent> REGISTER = new ArrayList<>();

    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");

    public static SoundEvent createSound(String name) {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res).setRegistryName(res);
        REGISTER.add(sound);
        return sound;
    }

    public static void init(MesonMod mod) {
        REGISTER.forEach(sound -> mod.register(sound, sound.getRegistryName()));
    }
}
