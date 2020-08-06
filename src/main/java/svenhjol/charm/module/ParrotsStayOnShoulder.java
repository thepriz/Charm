package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;

public class ParrotsStayOnShoulder extends MesonModule {
    @Module(description = "Parrots stay on your shoulder when jumping and falling. Crouch to make them dismount.", hasSubscriptions = true)
    public ParrotsStayOnShoulder() {}

    @SubscribeEvent
    public void onCrouch(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
            && event.player.world.getGameTime() % 10 == 0
            && event.player.isCrouching()
        ) {
            dismountParrot(event.player);
        }
    }

    public void dismountParrot(PlayerEntity player) {
        if (!player.world.isRemote) {
            final ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            if (!serverPlayer.getLeftShoulderEntity().isEmpty()) {
                ((PlayerEntityAccessor)serverPlayer).callSpawnShoulderEntity(serverPlayer.getLeftShoulderEntity());
                ((PlayerEntityAccessor)serverPlayer).callSetLeftShoulderEntity(new CompoundNBT());
            }
            if (!serverPlayer.getRightShoulderEntity().isEmpty()) {
                ((PlayerEntityAccessor)serverPlayer).callSpawnShoulderEntity(serverPlayer.getRightShoulderEntity());
                ((PlayerEntityAccessor)serverPlayer).callSetRightShoulderEntity(new CompoundNBT());
            }
        }
    }
}
