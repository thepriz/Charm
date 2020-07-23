package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class WitchesDropLuck extends MesonModule {
    public static double lootingBoost = 0.025D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed by the player.")
    public static double dropChance = 0.05D;

    @Module(description = "A witch has a chance to drop a Potion of Luck when killed by a player.", hasSubscriptions = true)
    public WitchesDropLuck() {}

    @SubscribeEvent
    public void onWitchDrops(LivingDropsEvent event) {
        if (!event.isCanceled()) {
            dropLuck(event.getEntityLiving(), event.getDrops(), event.getLootingLevel(), event.getSource());
        }
    }

    public void dropLuck(LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel, DamageSource damageSource) {
        if (!entity.world.isRemote
            && entity instanceof WitchEntity
            && damageSource.getTrueSource() instanceof PlayerEntity
            && entity.world.rand.nextFloat() <= (dropChance + lootingBoost * lootingLevel)
        ) {
            BlockPos pos = entity.func_233580_cy_();
            ItemStack potion = PotionHelper.getPotionItemStack(Potions.LUCK, 1);
            drops.add(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), potion));
        }
    }
}
