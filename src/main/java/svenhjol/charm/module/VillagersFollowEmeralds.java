package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;
import svenhjol.meson.mixin.GoalSelectorAccessor;

public class VillagersFollowEmeralds extends MesonModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Villagers are attracted when the player holds a block of emeralds.", hasSubscriptions = true)
    public VillagersFollowEmeralds() {}

    @Override
    public boolean test() {
        return !ModHelper.present("quark") || override;
    }

    @SubscribeEvent
    public void onVillagerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.isCanceled())
            followEmerald(event.getEntity());
    }

    public void followEmerald(Entity entity) {
        if (entity instanceof VillagerEntity) {
            VillagerEntity villager = (VillagerEntity) entity;

            Ingredient ingredient = Ingredient.fromItems(Blocks.EMERALD_BLOCK);

            if (((GoalSelectorAccessor)villager.goalSelector).getGoals().stream().noneMatch(g -> g.getGoal() instanceof TemptGoal))
                villager.goalSelector.addGoal(3, new TemptGoal(villager, 0.6, ingredient, false));
        }
    }
}
