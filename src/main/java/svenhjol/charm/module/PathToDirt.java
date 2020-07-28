package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class PathToDirt extends MesonModule {
    @Module(description = "Right-clicking on a grass path block with a shovel turns it back into dirt.", hasSubscriptions = true)
    public PathToDirt() {}

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled()) {
            boolean result = convertPath(event.getEntity(), event.getPos(), event.getHand(), event.getItemStack());
            if (result)
                event.setCanceled(true);
        }
    }

    public boolean convertPath(Entity entity, BlockPos pos, Hand hand, ItemStack stack) {
        if (entity.world != null
            && entity instanceof PlayerEntity
            && stack.getItem() instanceof HoeItem
        ) {
            BlockState state = entity.world.getBlockState(pos);
            if (state.getBlock() == Blocks.GRASS_PATH) {
                PlayerEntity player = (PlayerEntity)entity;
                player.swingArm(hand);

                if (!entity.world.isRemote) {
                    entity.world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 11);
                    entity.world.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    // damage the hoe a bit
                    stack.damageItem(1, player, p -> p.sendBreakAnimation(hand));
                    return true;
                }
            }
        }
        return false;
    }
}
