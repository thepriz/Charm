package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.StrayEntity;
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

public class StrayImprovements extends MesonModule {
    public static double lootingBoost = 0.1D;

    @Config(name = "Spawn anywhere in biome", description = "If true, strays can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop blue ice when killed", description = "If true, strays drop blue ice when killed.")
    public static boolean dropIce = true;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a stray dropping blue ices when it is killed.")
    public static double chance = 0.33D;

    @Module(description = "Strays spawn anywhere within their biome and have a chance to drop blue ice.", hasSubscriptions = true)
    public StrayImprovements() { }

    @SubscribeEvent
    public void onStrayDrops(LivingDropsEvent event) {
        if (!event.isCanceled()) {
            tryDropIce(event.getEntityLiving(), event.getDrops(), event.getLootingLevel());
        }
    }

    public void tryDropIce(LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel) {
        if (dropIce
            && !entity.world.isRemote
            && entity instanceof StrayEntity
            && entity.world.rand.nextFloat() <= (chance + (lootingBoost * lootingLevel))
        ) {
            BlockPos pos = entity.func_233580_cy_();
            ItemStack ice = new ItemStack(Items.BLUE_ICE);
            drops.add(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), ice));
        }
    }

    public static boolean canStraySpawnInLight(IWorld world, BlockPos pos) {
        return spawnAnywhere || world.canSeeSky(pos);
    }
}
