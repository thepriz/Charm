package svenhjol.meson.iface;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

@SuppressWarnings("unused")
public interface IMesonEnum extends IStringSerializable {
    @Override
    @SuppressWarnings("rawtypes")
    default String func_176610_l() {
        return ((Enum) this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getName() {
        return func_176610_l();
    }

    default String getCapitalizedName() {
        String name = func_176610_l();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
