package svenhjol.charm.module;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class UseTotemFromInventory extends MesonModule {
    @Module(description = "As long as a Totem of Undying is in your inventory, it will be consumed to protect you from death.", hasSubscriptions = true)
    public UseTotemFromInventory() {}

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!event.isCanceled()) {
            boolean usedTotem = useTotem(event.getEntityLiving());
            event.setCanceled(usedTotem); // instruct Forge not to actually kill the player
        }
    }

    public boolean useTotem(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            PlayerInventory inv = player.inventory;
            ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);

            // copypasta from net.minecraft.entity.LivingEntity#checkTotemDeathProtection

            if (inv.hasItemStack(totem)) {
                int slot = inv.findSlotMatchingUnusedItem(totem);
                inv.removeStackFromSlot(slot);

                // do the achievement stuff
                if (player instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
                    serverplayerentity.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, totem);
                }

                player.setHealth(1.0f);
                player.clearActivePotions();
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                player.world.setEntityState(player, (byte) 35);

                return true;
            }
        }
        return false;
    }
}
