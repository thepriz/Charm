package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.iface.Module;

public class FeatherFallingCrops extends MesonModule {
    @Module(description = "A player wearing feather falling enchanted boots will not trample crops.", hasSubscriptions = true)
    public FeatherFallingCrops() {}

    @SubscribeEvent
    public void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity)event.getEntity();
            
            if (EnchantmentsHelper.hasFeatherFalling(entity))
                event.setCanceled(true);
        }
    }
}
