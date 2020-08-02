package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;
import svenhjol.meson.mixin.ServerWorldAccessor;

import java.util.List;
import java.util.stream.Collectors;

public class SleepImprovements extends MesonModule {
    @Config(name = "Number of required players", description = "The number of players required to sleep in order to bring the next day.")
    public static int requiredPlayers = 1;

    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Allows the night to pass when a specified number of players are asleep.", hasSubscriptions = true)
    public SleepImprovements() {}

    @Override
    public boolean test() {
        return !ModHelper.present("quark") || override;
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (!event.isCanceled()
            && event.world != null
            && event.world.getGameTime() % 20 == 0
            && event.side != LogicalSide.CLIENT
            && WorldHelper.isDimension(event.world, new ResourceLocation("overworld"))
        ) {
            tryEndNight((ServerWorld)event.world);
        }
    }

    private void tryEndNight(ServerWorld world) {
        MinecraftServer server = world.getServer();

        int currentPlayerCount = world.getServer().getCurrentPlayerCount();
        if (currentPlayerCount < requiredPlayers)
            return;

        List<ServerPlayerEntity> validPlayers = server.getPlayerList().getPlayers().stream()
            .filter(p -> !p.isSpectator() && p.isPlayerFullyAsleep())
            .collect(Collectors.toList());

        if (validPlayers.size() < requiredPlayers)
            return;

        /** copypasta from {@link ServerWorld#tick} */
        if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            long l = world.getDayTime() + 24000L;
            world.func_241114_a_(net.minecraftforge.event.ForgeEventFactory.onSleepFinished(world, l - l % 24000L, world.getDayTime()));
        }

        ((ServerWorldAccessor)world).callWakeUpAllPlayers();
        if (world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE))
            ((ServerWorldAccessor)world).callResetRainAndThunder();
    }
}
