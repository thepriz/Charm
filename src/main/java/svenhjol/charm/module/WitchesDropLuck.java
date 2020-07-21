package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class WitchesDropLuck extends MesonModule {
    public static double lootingBoost = 0.025D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed by the player.")
    public static double dropChance = 0.05D;

    @Module(description = "A witch has a chance to drop a Potion of Luck when killed by a player.", hasSubscriptions = true)
    public WitchesDropLuck() {}

    @SubscribeEvent
    public void onWitchDrops(LivingDropsEvent event) {
        if (!event.isCanceled()
            && !event.getEntityLiving().world.isRemote
            && event.getEntityLiving() instanceof WitchEntity
            && event.getSource().getTrueSource() instanceof PlayerEntity
            && event.getEntityLiving().world.rand.nextFloat() <= (dropChance + lootingBoost * event.getLootingLevel())
        ) {
            Entity entity = event.getEntity();
            BlockPos pos = entity.func_233580_cy_();
            ItemStack potion = PotionHelper.getPotionItemStack(Potions.LUCK, 1);
            event.getDrops().add(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), potion));
        }
    }
}
