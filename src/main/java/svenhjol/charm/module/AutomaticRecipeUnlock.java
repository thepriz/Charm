package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class AutomaticRecipeUnlock extends MesonModule {
    @Module(description = "Unlocks all vanilla recipes.", hasSubscriptions = true)
    public AutomaticRecipeUnlock() { }

    @Override
    public boolean test() {
        return !ModHelper.present("quark");
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
