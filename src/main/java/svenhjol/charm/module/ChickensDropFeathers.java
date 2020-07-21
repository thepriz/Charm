package svenhjol.charm.module;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class ChickensDropFeathers extends MesonModule {
    @Module(description = "Chickens randomly drop feathers.", hasSubscriptions = true)
    public ChickensDropFeathers() {}

    @SubscribeEvent
    public void onChickenUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!event.isCanceled()
            && event.getEntityLiving() instanceof ChickenEntity
            && !event.getEntityLiving().world.isRemote
            && event.getEntityLiving().isAlive()
            && !event.getEntityLiving().isChild()
            && !((ChickenEntity) event.getEntityLiving()).isChickenJockey()
        ) {
            ChickenEntity chicken = (ChickenEntity) event.getEntityLiving();
            if (chicken.timeUntilNextEgg <= 1) {
                if (chicken.world.rand.nextFloat() < 0.2F) {
                    chicken.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (chicken.world.rand.nextFloat() - chicken.world.rand.nextFloat()) * 0.2F + 1.0F);
                    chicken.entityDropItem(Items.FEATHER);
                    chicken.timeUntilNextEgg = chicken.world.rand.nextInt(3000) + 3000;
                }
            }
        }
    }
}
