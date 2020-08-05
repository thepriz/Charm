package svenhjol.charm.base;

import svenhjol.charm.message.*;
import svenhjol.meson.MesonMod;
import svenhjol.meson.handler.PacketHandler;

public class CharmMessages {
    public static void init(MesonMod instance) {
        PacketHandler handler = instance.getPacketHandler();

        handler.register(ServerOpenCrafting.class, ServerOpenCrafting::encode, ServerOpenCrafting::decode, ServerOpenCrafting.Handler::handle);
        handler.register(ServerOpenEnderChest.class, ServerOpenEnderChest::encode, ServerOpenEnderChest::decode, ServerOpenEnderChest.Handler::handle);
        handler.register(ServerSortInventory.class, ServerSortInventory::encode, ServerSortInventory::decode, ServerSortInventory.Handler::handle);
        handler.register(ClientOpenInventory.class, ClientOpenInventory::encode, ClientOpenInventory::decode, ClientOpenInventory.Handler::handle);
        handler.register(ClientSetGlowingEntities.class, ClientSetGlowingEntities::encode, ClientSetGlowingEntities::decode, ClientSetGlowingEntities.Handler::handle);
    }
}
