package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class RemoveNitwits extends MesonModule {
    @Module(description = "When any action would cause a villager to become a nitwit, it becomes an unemployed villager instead.", hasSubscriptions = true)
    public RemoveNitwits() {}

    @SubscribeEvent
    public void onVillagerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.isCanceled()) {
            changeNitwitProfession(event.getEntity());
        }
    }

    public void changeNitwitProfession(Entity entity) {
        if (!entity.world.isRemote
            && entity instanceof VillagerEntity
        ) {
            VillagerEntity villager = (VillagerEntity) entity;
            VillagerData data = villager.getVillagerData();

            if (data.getProfession() == VillagerProfession.NITWIT) {
                villager.setVillagerData(data.withProfession(VillagerProfession.NONE));
                Meson.LOG.debug("Changed nitwit's profession to NONE: " + villager.getUniqueID());
            }
        }
    }
}
