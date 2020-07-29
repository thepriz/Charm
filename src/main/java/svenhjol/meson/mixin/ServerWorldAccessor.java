package svenhjol.meson.mixin;

import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {
    @Invoker
    void callWakeUpAllPlayers();

    @Invoker
    void callResetRainAndThunder();
}
