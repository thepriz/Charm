package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class HuskImprovements extends MesonModule {
    public static double lootingBoost = 0.1D;

    @Config(name = "Spawn anywhere in biome", description = "If true, husks can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop sand when killed", description = "If true, husks drop sand when killed.")
    public static boolean dropSand = true;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a husk dropping sand when it is killed.")
    public static double chance = 0.33D;

    @Module(description = "Husks spawn anywhere within their biome and have a chance to drop sand.")
    public HuskImprovements() {}

    @SubscribeEvent
    public void onHuskDrops(LivingDropsEvent event) {
        if (!event.isCanceled()) {
            tryDropSand(event.getEntityLiving(), event.getDrops(), event.getLootingLevel());
        }
    }

    public void tryDropSand(LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel) {
        if (dropSand
            && !entity.world.isRemote
            && entity instanceof HuskEntity
            && entity.world.rand.nextFloat() <= (chance + (lootingBoost * lootingLevel))
        ) {
            BlockPos pos = entity.func_233580_cy_();
            ItemStack sand = new ItemStack(Items.SAND);
            drops.add(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), sand));
        }
    }

    public static boolean canHuskSpawnInLight(IWorld world, BlockPos pos) {
        return spawnAnywhere || world.canSeeSky(pos);
    }
}
