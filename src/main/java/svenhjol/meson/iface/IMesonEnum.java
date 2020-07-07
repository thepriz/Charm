package svenhjol.meson.iface;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

@SuppressWarnings("unused")
public interface IMesonEnum extends IStringSerializable {
    @Override
    @SuppressWarnings("rawtypes")
    default String getString() {
        return ((Enum) this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getName() {
        return getString();
    }

    default String getCapitalizedName() {
        String name = getString();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
