package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class CaveSpidersDropCobwebs extends MesonModule {
    public static double lootingBoost = 0.1D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a cave spider dropping a cobweb when it is killed.")
    public static double chance = 0.33D;

    @Module(description = "Cave spiders have a chance to drop cobwebs.", hasSubscriptions = true)
    public CaveSpidersDropCobwebs() {}

    @SubscribeEvent
    public void onCaveSpiderDrops(LivingDropsEvent event) {
        if (!event.isCanceled()) {
            tryDropCobwebs(event.getEntityLiving(), event.getDrops(), event.getLootingLevel());
        }
    }

    public void tryDropCobwebs(LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel) {
        if (!entity.world.isRemote
            && entity instanceof CaveSpiderEntity
            && entity.world.rand.nextFloat() <= (chance + (lootingBoost * lootingLevel))
        ) {
            BlockPos pos = entity.func_233580_cy_();
            ItemStack cobweb = new ItemStack(Items.COBWEB);
            drops.add(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), cobweb));
        }
    }
}
