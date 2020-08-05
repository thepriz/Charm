package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class AutomaticRecipeUnlock extends MesonModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Unlocks all vanilla recipes.", hasSubscriptions = true)
    public AutomaticRecipeUnlock() { }

    @Override
    public boolean test() {
        return !ModHelper.present("quark") || override;
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        unlockRecipes(event.getPlayer());
    }

    public void unlockRecipes(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            RecipeManager recipeManager = player.world.getRecipeManager();
            Collection<IRecipe<?>> allRecipes = recipeManager.getRecipes();
            player.unlockRecipes(allRecipes);
        }
    }
}
