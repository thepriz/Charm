package svenhjol.charm.decoration.state;

import net.minecraft.util.IStringSerializable;

public enum LockerType implements IStringSerializable {
    SINGLE("single", 0),
    BOTTOM("bottom", 2),
    TOP("top", 1);

    public static final LockerType[] VALUES = values();
    private final String name;
    private final int opposite;

    LockerType(String name, int oppositeIn) {
        this.name = name;
        this.opposite = oppositeIn;
    }

    public String getName() {
        return this.name;
    }

    public LockerType opposite() {
        return VALUES[this.opposite];
    }
}
