package svenhjol.meson.helper;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.Random;

public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
            return false;
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static void openInventory() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        mc.displayGuiScreen(new InventoryScreen(mc.player));
    }
}
