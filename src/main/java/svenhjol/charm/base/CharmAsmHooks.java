package svenhjol.charm.base;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import svenhjol.charm.module.HuskImprovements;
import svenhjol.charm.module.LanternImprovements;
import svenhjol.charm.module.StackableBooks;
import svenhjol.charm.module.StackablePotions;
import svenhjol.meson.Meson;

public class CharmAsmHooks {
    /**
     * If true, the potion glint will not be applied to the PotionItem.
     * Disable the RemovePotionGlint to restore vanilla behavior.
     * @return True to disable potion glint.
     */
    public static boolean removePotionGlint() {
        return Meson.enabled("charm:remove_potion_glint");
    }

    /**
     * Overrides the vanilla default minimum XP of 1 (> 0) to zero (> -1).
     * @return -1 if NoAnvilMinimumXp is enabled, vanilla default of 0 if not enabled.
     */
    public static int getMinimumRepairCost() {
        return Meson.enabled("charm:no_anvil_minimum_xp") ? -1 : 0;
    }

    /**
     * Simply checks if the StackablePotions module is enabled and that the input stack can be added.
     * Returning true forces Forge's BrewingRecipeRegistry#isValidInput method to return true.
     * @param stack ItemStack to verify as valid brewing stand input.
     * @return True to force BrewingRecipeRegistry#isValidInput to be valid.
     */
    public static boolean checkBrewingStandStack(ItemStack stack) {
        return Meson.enabled("charm:stackable_potions") && StackablePotions.isValidItemStack(stack);
    }

    /**
     * StackableBooks can check if the middle (material) slot of the anvil is a stack of enchanted books.
     * If it is, it returns a stack depleted by 1 book.  If not, just returns an empty item (vanilla)
     * @param inventory The full anvil inventory. Middle slot is slot 1.
     * @return ItemStack The itemstack to set as the middle slot after anvil op is complete.
     */
    public static ItemStack getAnvilMaterialItem(IInventory inventory) {
        if (Meson.enabled("charm:stackable_books"))
            return StackableBooks.checkItemStack(inventory.getStackInSlot(1));

        return ItemStack.EMPTY;
    }

    /**
     * Forge has a check that prevents modders from adding new state props.
     * This must be bypassed by lantern improvements so that waterlogging can be added.
     * @param block The block that state props can be added to.
     * @return True to bypass Forge's state prop check.
     */
    public static boolean bypassForgeStateCheck(Block block) {
        return Meson.enabled("charm:lantern_improvements") && LanternImprovements.checkLantern(block);
    }

    public static boolean canHuskSpawnInLight(IWorld world, BlockPos pos) {
        if (Meson.enabled("charm:husk_improvements"))
            return HuskImprovements.canHuskSpawnInLight(world, pos);

        return world.canSeeSky(pos);
    }

    public static boolean redrawInventoryScreen() {
        return Meson.enabled("charm:crafting_inventory");
    }
}
