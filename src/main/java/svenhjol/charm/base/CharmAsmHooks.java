package svenhjol.charm.base;

import net.minecraft.item.ItemStack;
import svenhjol.charm.modules.StackablePotionsModule;
import svenhjol.meson.Meson;

public class CharmAsmHooks {
    /**
     * If true, the potion glint will not be applied to the PotionItem.
     * Disable the RemovePotionGlint to restore vanilla behavior.
     * @return True to disable potion glint.
     */
    public static boolean removePotionGlint() {
        return Meson.enabled("charm:remove_potion_glint_module");
    }

    /**
     * Overrides the vanilla default minimum XP of 1 (> 0) to zero (> -1).
     * @return -1 if NoAnvilMinimumXp is enabled, vanilla default of 0 if not enabled.
     */
    public static int getMinimumRepairCost() {
        return Meson.enabled("charm:no_anvil_minimum_xp_module") ? -1 : 0;
    }

    /**
     * Simply checks if the StackablePotions module is enabled and that the input stack can be added.
     * Returning true forces Forge's BrewingRecipeRegistry#isValidInput method to return true.
     * @param stack ItemStack to verify as valid brewing stand input.
     * @return True to force BrewingRecipeRegistry#isValidInput to be valid.
     */
    public static boolean checkBrewingStandStack(ItemStack stack) {
        return Meson.enabled("charm:stackable_potions_module") && StackablePotionsModule.isValidItemStack(stack);
    }
}
