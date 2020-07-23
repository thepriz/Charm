package svenhjol.charm.module;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MoreVillagerTrades extends MesonModule {
    @Module(description = "", hasSubscriptions = true)
    public MoreVillagerTrades() {}

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
        VillagerProfession profession = event.getType();

        if (profession.getRegistryName() == null)
            return;

        switch (profession.getRegistryName().getPath()) {
            case "librarian":
                trades.get(3).add(new EmeraldsForEnchantedBookTrade());
                break;

            case "butcher":
                trades.get(3).add(new ItemForZombieFleshTrade(Items.PORKCHOP, 3, 6));
                break;

            case "leatherworker":
                trades.get(3).add(new ItemForZombieFleshTrade(Items.LEATHER, 1, 3));
                break;

            case "armorer":
            case "weaponsmith":
            case "toolsmith":
                trades.get(2).add(new RepairedAnvilForDamagedAnvilTrade());
                break;

            default:
                break;
        }
    }

    public static class RepairedAnvilForDamagedAnvilTrade implements VillagerTrades.ITrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, Random rand) {
            ItemStack in1 = new ItemStack(rand.nextFloat() > 0.5F ? Items.DAMAGED_ANVIL : Items.CHIPPED_ANVIL);
            ItemStack in2 = new ItemStack(Items.IRON_INGOT, 4 + rand.nextInt(4));
            ItemStack out = new ItemStack(Items.ANVIL);
            return new MerchantOffer(in1, in2, out, 10, 10, 0.2F);
        }
    }

    public static class ItemForZombieFleshTrade implements VillagerTrades.ITrade {
        private final Item trade;
        private final int min;
        private final int max;

        public ItemForZombieFleshTrade(Item trade, int min, int max) {
            this.trade = trade;
            this.min = min;
            this.max = max;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, Random rand) {
            int count = rand.nextInt(max - min) + min;
            ItemStack in1 = new ItemStack(Items.ROTTEN_FLESH, count * 4);
            ItemStack out = new ItemStack(trade, count);
            return new MerchantOffer(in1, out, 10, 5, 0.2F);
        }
    }

    public static class EmeraldsForEnchantedBookTrade implements VillagerTrades.ITrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, Random rand) {
            int count = rand.nextInt(1) + 2;
            ItemStack in1 = new ItemStack(Items.ENCHANTED_BOOK); // any book
            ItemStack out = new ItemStack(Items.EMERALD, count);
            return new MerchantOffer(in1, out, 10, 5, 0.2F);
        }
    }
}
