package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class CaveSpidersDropCobwebs extends MesonModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum cobwebs dropped when cave spider is killed.")
    public static int maxDrops = 2;

    @Module(description = "Cave spiders have a chance to drop cobwebs.", hasSubscriptions = true)
    public CaveSpidersDropCobwebs() {}

    @SubscribeEvent
    public void onCaveSpiderDrops(LivingDropsEvent event) {
        if (!event.isCanceled())
            tryDropCobweb(event.getEntityLiving(), event.getDrops(), event.getLootingLevel());
    }

    public void tryDropCobweb(LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel) {
        if (!entity.world.isRemote
            && entity instanceof CaveSpiderEntity
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getPosition();

            int amount = ItemHelper.getAmountWithLooting(world.rand, (int)maxDrops, lootingLevel, (float)lootingBoost);
            drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.COBWEB, amount)));
        }
    }
}
