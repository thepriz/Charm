package svenhjol.charm.tweaks.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.IMesonMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ServerInteractCauldron implements IMesonMessage {
    public static List<BiConsumer<Context, CompoundNBT>> runOnUpdate = new ArrayList<>();

    public ServerInteractCauldron() {
    }

    @SuppressWarnings("EmptyMethod")
    public static void encode(ServerInteractCauldron msg, PacketBuffer buf) {
    }

    public static ServerInteractCauldron decode(PacketBuffer buf) {
        return new ServerInteractCauldron();
    }

    public static class Handler {
        public static void handle(final ServerInteractCauldron msg, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Context context = ctx.get();
                ServerPlayerEntity player = context.getSender();
                if (player == null) return;

                ItemStack item = null;
                SoundEvent sound = null;

                ItemStack held = player.getHeldItemMainhand();
                if (held.getItem() == Items.GLASS_BOTTLE) {
                    item = PotionHelper.getFilledWaterBottle();
                    sound = SoundEvents.ITEM_BOTTLE_FILL;
                } else if (held.getItem() == Items.BUCKET) {
                    item = new ItemStack(Items.WATER_BUCKET);
                    sound = SoundEvents.ITEM_BUCKET_FILL;
                }

                if (item != null) {
                    PlayerHelper.setHeldItem(player, Hand.MAIN_HAND, item);
                    player.world.playSound(null, player.func_233580_cy_(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
