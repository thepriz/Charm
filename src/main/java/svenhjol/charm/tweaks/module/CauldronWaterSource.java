package svenhjol.charm.tweaks.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tweaks.message.ServerInteractCauldron;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.CauldronHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Cauldrons can be used as a permanent water source when sneaking.")
public class CauldronWaterSource extends MesonModule {
    @SubscribeEvent
    public void onBucketUse(FillBucketEvent event) {
        if (event.getEntityLiving() != null
            && event.getEntityLiving() instanceof PlayerEntity
            && PlayerHelper.isCrouching((PlayerEntity) event.getEntityLiving())
            && !event.isCanceled()
        ) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            if (event.getTarget() != null) {
                World world = player.getEntityWorld();
                BlockPos hit = new BlockPos(event.getTarget().getHitVec());
                BlockPos use = compensateForShenanigans(world, hit);

                if (use != null && CauldronHelper.isFull(world.getBlockState(use)))
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onCauldronUse(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON
            && event.getPlayer() != null
            && event.getHand() == Hand.MAIN_HAND
        ) {
            PlayerEntity player = event.getPlayer();
            World world = player.world;
            ItemStack held = player.getHeldItem(event.getHand());
            BlockState state = event.getWorld().getBlockState(event.getPos());

            if (PlayerHelper.isCrouching(player)
                && CauldronHelper.isFull(state)
            ) {
                if (world.isRemote) {
                    Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendToServer(new ServerInteractCauldron());
                }

                event.setUseItem(Event.Result.DENY);
            } else {

                // don't activate cauldron if stack contains multiple water bottles
                if (held.getItem() == Items.POTION
                    && PotionUtils.getPotionFromItem(held) == Potions.WATER
                    && held.getCount() > 1
                ) {
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * This is probably a vanilla bug but I can't be bothered spending time patching it.
     */
    @Nullable
    private BlockPos compensateForShenanigans(World world, BlockPos hit) {
        BlockPos offset = new BlockPos(hit);

        if (world.isAirBlock(hit)) {
            BlockPos tryNorth = new BlockPos(offset.north());
            BlockPos tryWest = new BlockPos(offset.west());

            if (world.getBlockState(tryNorth).getBlock() == Blocks.CAULDRON) {
                return tryNorth;
            } else if (world.getBlockState(tryWest).getBlock() == Blocks.CAULDRON) {
                return tryWest;
            }
        }

        return null;
    }
}
