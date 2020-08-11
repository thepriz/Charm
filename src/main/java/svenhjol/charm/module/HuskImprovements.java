package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HuskEntity;
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

public class HuskImprovements extends MesonModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, husks can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop sand when killed", description = "If true, husks drop sand when killed.")
    public static boolean dropSand = true;

    @Config(name = "Maximum drops", description = "Maximum sand dropped when husk is killed.")
    public static double maxDrops = 2;

    @Module(description = "Husks spawn anywhere within their biome and have a chance to drop sand.", hasSubscriptions = true)
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
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getPosition();

            int amount = ItemHelper.getAmountWithLooting(world.rand, (int)maxDrops, lootingLevel, (float)lootingBoost);
            drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.SAND, amount)));
        }
    }

    public static boolean canHuskSpawnInLight(IWorld world, BlockPos pos) {
        return spawnAnywhere || world.canSeeSky(pos);
    }
}
