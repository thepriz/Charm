package svenhjol.meson.iface;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

@SuppressWarnings({"NullableProblems", "rawtypes"})
public interface IMesonEnum extends IStringSerializable {
    @Override
    default String getString() {
        return ((Enum)this).name().toLowerCase(Locale.ENGLISH);
    }
}
