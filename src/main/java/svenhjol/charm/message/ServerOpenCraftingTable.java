package svenhjol.charm.message;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import svenhjol.charm.module.CraftingInventory;
import svenhjol.meson.message.IMesonMessage;

import java.util.function.Supplier;

@SuppressWarnings("EmptyMethod")
public class ServerOpenCraftingTable implements IMesonMessage {
    public static void encode(ServerOpenCraftingTable msg, PacketBuffer buf) {
    }

    public static ServerOpenCraftingTable decode(PacketBuffer buf) {
        return new ServerOpenCraftingTable();
    }

    public static class Handler {
        public static void handle(final ServerOpenCraftingTable msg, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Context context = ctx.get();
                ServerPlayerEntity player = context.getSender();

                // guard against player not having a crafting table
                if (player == null || !player.inventory.hasItemStack(new ItemStack(Blocks.CRAFTING_TABLE)))
                    return;

                CraftingInventory.openContainer(player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
