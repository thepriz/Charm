package svenhjol.charm.base;

import svenhjol.charm.message.ClientOpenInventory;
import svenhjol.charm.message.ClientSetGlowingEntities;
import svenhjol.charm.message.ServerOpenCraftingTable;
import svenhjol.meson.MesonMod;
import svenhjol.meson.handler.PacketHandler;

public class CharmMessages {
    public static void init(MesonMod instance) {
        PacketHandler handler = instance.getPacketHandler();

        handler.register(ServerOpenCraftingTable.class, ServerOpenCraftingTable::encode, ServerOpenCraftingTable::decode, ServerOpenCraftingTable.Handler::handle);
        handler.register(ClientOpenInventory.class, ClientOpenInventory::encode, ClientOpenInventory::decode, ClientOpenInventory.Handler::handle);
        handler.register(ClientSetGlowingEntities.class, ClientSetGlowingEntities::encode, ClientSetGlowingEntities::decode, ClientSetGlowingEntities.Handler::handle);
    }
}
