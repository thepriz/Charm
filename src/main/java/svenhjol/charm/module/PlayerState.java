package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.client.PlayerStateClient;
import svenhjol.charm.message.ClientUpdatePlayerState;
import svenhjol.charm.message.ServerUpdatePlayerState;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PlayerState extends MesonModule {
    public static List<BiConsumer<ServerPlayerEntity, CompoundNBT>> listeners = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public static PlayerStateClient client;

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional world state will be synchronised to the client.")
    public static int serverStateInverval = 120;

    @Module(description = "Synchronize additional state from server to client.", hasSubscriptions = true)
    public PlayerState() {}

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        client = new PlayerStateClient();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
            && event.player.world.isRemote
            && event.player.world.getGameTime() % serverStateInverval == 0
        ) {
            this.mod.getPacketHandler().sendToServer(new ServerUpdatePlayerState());
        }
    }

    public static void serverCallback(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getPosition();

        final long dayTime = world.getDayTime() % 24000;

        CompoundNBT tag = new CompoundNBT();

        tag.putBoolean("mineshaft", WorldHelper.isInsideStructure(world, pos, Structure.field_236367_c_));
        tag.putBoolean("stronghold", WorldHelper.isInsideStructure(world, pos, Structure.field_236375_k_));
        tag.putBoolean("fortress", WorldHelper.isInsideStructure(world, pos, Structure.field_236378_n_));
        tag.putBoolean("shipwreck", WorldHelper.isInsideStructure(world, pos, Structure.field_236373_i_));
        tag.putBoolean("village", WorldHelper.isInsideStructure(world, pos, Structure.field_236381_q_));
        tag.putBoolean("day", dayTime > 0 && dayTime < 12700);

        if (Quark.compat != null && Quark.compat.hasBigDungeons())
            tag.putBoolean("big_dungeon", Quark.compat.isInsideBigDungeon(world, pos));

        // send updated player data to listeners
        listeners.forEach(action -> action.accept(player, tag));

        // send updated player data to client
        Meson.getMod(Charm.MOD_ID).getPacketHandler().sendToPlayer(new ClientUpdatePlayerState(tag), player);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientCallback(CompoundNBT data) {
        client.mineshaft = data.getBoolean("mineshaft");
        client.stronghold = data.getBoolean("stronghold");
        client.fortress = data.getBoolean("fortress");
        client.shipwreck = data.getBoolean("shipwreck");
        client.village = data.getBoolean("village");
        client.bigDungeon = data.getBoolean("big_dungeon");
        client.isDaytime = data.getBoolean("day");
    }
}
