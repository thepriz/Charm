package svenhjol.charm.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

public class ExtractEnchantments extends MesonModule {
    @Config(name = "Initial XP cost", description = "Initial XP cost before adding XP equivalent to the enchantment level(s) of the item.")
    public static int initialCost = 2;

    @Module(description = "Extract enchantments from any enchanted item into an empty book.", hasSubscriptions = true)
    public ExtractEnchantments() {}

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;
        if (right.getItem() != Items.BOOK) return;

        ListNBT leftTags = left.getEnchantmentTagList();
        ListNBT rightTags = right.getEnchantmentTagList();
        if (leftTags.isEmpty() || !rightTags.isEmpty()) return;

        int cost = initialCost;

        Map<Enchantment, Integer> inEnchants = EnchantmentHelper.getEnchantments(left);
        Map<Enchantment, Integer> outEnchants = new HashMap<>();

        // get all enchantments from the left item and create a map of enchantments for the output
        for (Map.Entry<Enchantment, Integer> entry : inEnchants.entrySet()) {
            Enchantment ench = entry.getKey();
            if (ench == null) return;

            int level = entry.getValue();
            if (level > 0 && ench.isAllowedOnBooks()) {
                outEnchants.put(ench, level);
                cost += level;
            }
        }

        if (outEnchants.values().size() == 0) {
            event.setCanceled(true);
            return;
        }

        // add repair cost on the input item
        if (left.getTag() != null && !left.getTag().isEmpty()) {
            cost += left.getTag().getInt("RepairCost");
        }

        // apply enchantments to the book
        out = new ItemStack(Items.ENCHANTED_BOOK);
        outEnchants.forEach((e, level) -> EnchantedBookItem.addEnchantment(out, new EnchantmentData(e, level)));

        // set the display name on the returned item
        String name = event.getName();
        if (name != null && !name.isEmpty()) {
            out.setDisplayName(new StringTextComponent(name));
        }

        event.setCost(cost);
        event.setMaterialCost(1);
        event.setOutput(out);
    }
}
