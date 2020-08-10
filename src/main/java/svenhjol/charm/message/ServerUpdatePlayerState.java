package svenhjol.charm.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import svenhjol.charm.module.PlayerState;
import svenhjol.meson.message.IMesonMessage;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ServerUpdatePlayerState implements IMesonMessage {
    public ServerUpdatePlayerState() { }

    public static void encode(ServerUpdatePlayerState msg, PacketBuffer buf) { }

    public static ServerUpdatePlayerState decode(PacketBuffer buf) {
        return new ServerUpdatePlayerState();
    }

    public static class Handler {
        public static void handle(final ServerUpdatePlayerState msg, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                NetworkEvent.Context context = ctx.get();
                ServerPlayerEntity player = context.getSender();

                if (player == null)
                    return;

                PlayerState.serverCallback(player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
