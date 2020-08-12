package svenhjol.charm.module;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.block.EnderPearlBlock;
import svenhjol.charm.goal.FormEndermiteGoal;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockOfEnderPearls extends MesonModule {
    public static EnderPearlBlock ENDER_PEARL_BLOCK;

    public static int teleportRange = 8;

    @Config(
        name = "Chorus fruit teleport",
        description = "If true, eating a chorus fruit while in range of an ender pearl block will teleport you to it.")
    public static boolean chorusTeleport = true;

    @Config(
        name = "Convert silverfish to endermite",
        description = "If true, a silverfish has a chance to be converted into an endermite when next to an ender pearl block."
    )
    public static boolean convertSilverfish = true;
    
    @Module(description = "Ender pearl storage. Eating a chorus fruit teleports you to the nearest block.", hasSubscriptions = true)
    public BlockOfEnderPearls() {}

    @Override
    public void init() {
        ENDER_PEARL_BLOCK = new EnderPearlBlock(this);
    }

    @SubscribeEvent
    public void onSilverfishEnteringChunk(EnteringChunk event) {
        if (convertSilverfish && !event.isCanceled())
            addGoalToSilverfish(event.getEntity());
    }

    public static boolean chorusTeleport(LivingEntity entity, ItemStack stack) {
        if (chorusTeleport
            && entity instanceof PlayerEntity
            && stack.getItem() == Items.CHORUS_FRUIT
            && !entity.world.isRemote
        ) {
            boolean didTeleport = false;
            World world = entity.world;
            PlayerEntity player = (PlayerEntity) entity;
            Map<Double, BlockPos> positions = new HashMap<>();
            BlockPos playerPos = player.getPosition();
            BlockPos targetPos = null;

            // find the blocks around the player
            BlockPos.getAllInBox(playerPos.add(-teleportRange, -teleportRange, -teleportRange), playerPos.add(teleportRange, teleportRange, teleportRange)).forEach(pos -> {
                if (world.getBlockState(pos).getBlock() == ENDER_PEARL_BLOCK && !pos.up(1).equals(playerPos)) {

                    // should be able to stand on it
                    if (world.getBlockState(pos.up(1)).getMaterial() == Material.AIR && world.getBlockState(pos.up(2)).getMaterial() == Material.AIR)
                        positions.put(WorldHelper.getDistanceSq(playerPos, pos.up(1)), pos.up(1));
                }
            });

            // get the closest position by finding the smallest distance
            if (!positions.isEmpty())
                targetPos = positions.get(Collections.min(positions.keySet()));

            if (targetPos != null) {
                double x = targetPos.getX() + 0.5D;
                double y = targetPos.getY();
                double z = targetPos.getZ() + 0.5D;
                if (player.attemptTeleport(x, y, z, true)) {
                    didTeleport = true;

                    // play sound at original location and new location
                    world.playSound(null, x, y, z, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                }
            }

            if (didTeleport) {
                player.getCooldownTracker().setCooldown(Items.CHORUS_FRUIT, 20);
                if (!player.abilities.isCreativeMode)
                    stack.shrink(1);

                return true;
            }
        }
        return false;
    }

    private void addGoalToSilverfish(Entity entity) {
        if (entity instanceof SilverfishEntity) {
            SilverfishEntity silverfish = (SilverfishEntity) entity;

            if (silverfish.goalSelector.getRunningGoals().noneMatch(g -> g.getGoal() instanceof FormEndermiteGoal))
                silverfish.goalSelector.addGoal(2, new FormEndermiteGoal(silverfish));
        }
    }
}
