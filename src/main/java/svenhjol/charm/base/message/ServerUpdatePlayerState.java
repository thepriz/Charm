package svenhjol.charm.base.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import svenhjol.charm.Charm;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.IMesonMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Server assembles list of state for client, like inside structures, is day or night...
 */
@SuppressWarnings("unused")
public class ServerUpdatePlayerState implements IMesonMessage {
    public static List<BiConsumer<Context, CompoundNBT>> runOnUpdate = new ArrayList<>();

    public ServerUpdatePlayerState() {
    }

    @SuppressWarnings("EmptyMethod")
    public static void encode(ServerUpdatePlayerState msg, PacketBuffer buf) {
    }

    public static ServerUpdatePlayerState decode(PacketBuffer buf) {
        return new ServerUpdatePlayerState();
    }

    public static class Handler {
        public static void handle(final ServerUpdatePlayerState msg, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Context context = ctx.get();
                ServerPlayerEntity player = context.getSender();
                if (player == null) return;

                ServerWorld world = player.getServerWorld();
                BlockPos pos = player.func_233580_cy_();

                final long dayTime = world.getDayTime() % 24000;

                CompoundNBT nbt = new CompoundNBT();

                nbt.putBoolean("mineshaft", WorldHelper.isPositionInsideStructure(world, pos, Structure.field_236367_c_));
                nbt.putBoolean("stronghold", WorldHelper.isPositionInsideStructure(world, pos, Structure.field_236375_k_));
                nbt.putBoolean("fortress", WorldHelper.isPositionInsideStructure(world, pos, Structure.field_236378_n_));
                nbt.putBoolean("shipwreck", WorldHelper.isPositionInsideStructure(world, pos, Structure.field_236373_i_));
                nbt.putBoolean("village", WorldHelper.isPositionInsideStructure(world, pos, Structure.field_236381_q_));
                nbt.putBoolean("day", dayTime > 0 && dayTime < 12700);

                if (Charm.quarkCompat != null && Meson.isModuleEnabled(new ResourceLocation("quark:big_dungeons"))) {
                    nbt.putBoolean("big_dungeon", Charm.quarkCompat.isInsideBigDungeon(world, pos));
                }

                // update subscribed mods
                runOnUpdate.forEach(action -> action.accept(context, nbt));

                Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendTo(new ClientUpdatePlayerState(nbt), player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
