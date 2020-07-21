package svenhjol.charm.modules;

import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class VillagersFollowEmeralds extends MesonModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Villagers are attracted when the player holds an emerald.", hasSubscriptions = true)
    public VillagersFollowEmeralds() {}

    @Override
    public boolean test() {
        return !ForgeHelper.isModPresent("quark") || override;
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.isCanceled()
            && event.getEntity() instanceof VillagerEntity
        ) {
            VillagerEntity villager = (VillagerEntity)event.getEntity();
            if (!villager.goalSelector.goals.stream().anyMatch(g -> g.getGoal() instanceof TemptGoal)) {
                villager.goalSelector.addGoal(3, new TemptGoal(villager, 0.6, Ingredient.fromItems(Items.EMERALD), false));
            }
        }
    }
}
