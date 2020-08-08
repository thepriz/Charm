package svenhjol.charm.base;

import net.minecraft.inventory.container.Container;
import svenhjol.charm.container.BookcaseContainer;
import svenhjol.charm.container.CrateContainer;

public class CharmAsmHooks {
    public static boolean quarkInventoryTransferHook(Container container) {
        return container instanceof CrateContainer
            || container instanceof BookcaseContainer;
    }
}
