package svenhjol.charm.base;

import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import svenhjol.charm.client.MusicClient;
import svenhjol.charm.module.*;
import svenhjol.meson.Meson;

import java.util.Random;

@SuppressWarnings("unused")
public class CharmAsmHooks {
    public static boolean canHuskSpawnInLight(IWorld world, BlockPos pos) {
        if (Meson.enabled("charm:husk_improvements"))
            return HuskImprovements.canHuskSpawnInLight(world, pos);

        return world.canSeeSky(pos);
    }

    public static boolean canStraySpawnInLight(IWorld world, BlockPos pos) {
        if (Meson.enabled("charm:stray_improvements"))
            return StrayImprovements.canStraySpawnInLight(world, pos);

        return world.canSeeSky(pos);
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
     * Overrides the vanilla default maximum XP of 40 to a 'big number'.
     * @return A silly big number if NoAnvilMaximumXp is enabled, vanilla default of 40 if not enabled.
     */
    public static int getMaximumRepairCost() {
        int res =  Meson.enabled("charm:anvil_improvements") ? Integer.MAX_VALUE : 40;

        return res;
    }

    /**
     * Overrides the vanilla default minimum XP of 1 (> 0) to zero (> -1).
     * @return -1 if NoAnvilMinimumXp is enabled, vanilla default of 0 if not enabled.
     */
    public static int getMinimumRepairCost() {
        return Meson.enabled("charm:anvil_improvements") ? -1 : 0;
    }

    public static boolean handleMusicPlaying(BackgroundMusicSelector music) {
        return MusicClient.enabled && MusicClient.handlePlaying(music);
    }

    public static boolean handleMusicStop() {
        return MusicClient.enabled && MusicClient.handleStop();
    }

    public static boolean handleMusicTick(ISound currentMusic) {
        return MusicClient.enabled && MusicClient.handleTick(currentMusic);
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack) {
        return Meson.enabled("charm:lightweight_armor_invisibility")
            && ArmorInvisibility.isArmorInvisible(entity, stack);
    }

    public static boolean isSignalFireInRange(World world, BlockPos pos) {
        return WanderingTraderImprovements.isSignalFireInRange(world, pos);
    }

    /**
     * If true, the potion glint will not be applied to the PotionItem.
     * Disable the RemovePotionGlint to restore vanilla behavior.
     * @return True to disable potion glint.
     */
    public static boolean removePotionGlint() {
        return Meson.enabled("charm:remove_potion_glint");
    }

    public static boolean parrotStayOnShoulder() {
        return Meson.enabled("charm:parrots_stay_on_shoulder");
    }

    public static void mineshaftGeneration(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (Meson.enabled("charm:mineshaft_improvements")) {
            MineshaftImprovements.generatePiece(piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

}
