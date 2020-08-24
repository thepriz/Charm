package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class StrayImprovements extends MesonModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, strays can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop blue ice when killed", description = "If true, strays drop blue ice when killed.")
    public static boolean dropIce = true;

    @Config(name = "Maximum drops", description = "Maximum blue ice dropped when stray is killed.")
    public static int maxDrops = 2;

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
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getPosition();

            int amount = ItemHelper.getAmountWithLooting(world.rand, (int)maxDrops, lootingLevel, (float)lootingBoost);
            drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.BLUE_ICE, amount)));
        }
    }

    public static boolean canStraySpawnInLight(IWorld world, BlockPos pos) {
        return spawnAnywhere || world.canSeeSky(pos);
    }
}
