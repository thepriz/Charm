package svenhjol.charm.modules;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class AutomaticRecipeUnlock extends MesonModule {
    @Module(description = "Unlocks all vanilla recipes.", hasSubscriptions = true)
    public AutomaticRecipeUnlock() { }

    @Override
    public boolean test() {
        return !ForgeHelper.isModPresent("quark");
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            RecipeManager recipeManager = event.getPlayer().world.getRecipeManager();
            Collection<IRecipe<?>> allRecipes = recipeManager.getRecipes();
            event.getPlayer().unlockRecipes(allRecipes);
        }
    }
}
