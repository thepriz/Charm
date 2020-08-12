package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class DirtToPath extends MesonModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    @Module(description = "Right-clicking dirt with a shovel turns it into grass path.", hasSubscriptions = true)
    public DirtToPath() {}

    @Override
    public boolean shouldSetup() {
        return !ModHelper.present("quark") || override;
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled()) {
            boolean result = convertDirt(event.getEntity(), event.getPos(), event.getHand(), event.getItemStack());
            if (result)
                event.setCanceled(true);
        }
    }

    public boolean convertDirt(Entity entity, BlockPos pos, Hand hand, ItemStack stack) {
        if (entity.world != null
            && entity instanceof PlayerEntity
            && stack.getItem() instanceof ShovelItem
        ) {
            BlockState state = entity.world.getBlockState(pos);
            if (state.getBlock() == Blocks.DIRT) {
                PlayerEntity player = (PlayerEntity)entity;
                player.swingArm(hand);

                if (!entity.world.isRemote) {
                    entity.world.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState(), 11);
                    entity.world.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    // damage the shovel a bit
                    stack.damageItem(1, player, p -> p.sendBreakAnimation(hand));
                    return true;
                }
            }
        }
        return false;
    }
}
