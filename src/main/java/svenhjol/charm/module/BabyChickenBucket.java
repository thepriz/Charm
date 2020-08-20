package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.item.BabyChickenBucketItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Module;

public class BabyChickenBucket extends MesonModule {
    public static BabyChickenBucketItem BABY_CHICKEN_BUCKET_ITEM;

    @Module(description = "Right-click a chicken with a bucket to capture it. Right-click again to release it.", hasSubscriptions = true)
    public BabyChickenBucket() {}

    @Override
    public void init() {
        BABY_CHICKEN_BUCKET_ITEM = new BabyChickenBucketItem(this);
    }


    @SubscribeEvent
    public void onChickenInteract(EntityInteract event) {
        if (!event.isCanceled())
            capture(event.getTarget(), event.getPlayer(), event.getHand());
    }

    private void capture(Entity entity, PlayerEntity player, Hand hand) {
        if (!entity.world.isRemote
            && entity instanceof ChickenEntity
            && ((ChickenEntity)entity).getHealth() > 0
        ) {
            ChickenEntity Chicken = (ChickenEntity)entity;
            ItemStack held = player.getHeldItem(hand);
            if (!Chicken.isChild()) return;

            if (held.isEmpty() || held.getItem() != Items.BUCKET)
                return;

            ItemStack BabyChickenBucket = new ItemStack(BABY_CHICKEN_BUCKET_ITEM);
            CompoundNBT tag = Chicken.serializeNBT();
            ItemNBTHelper.setCompound(BabyChickenBucket, BabyChickenBucketItem.STORED_BABY_CHICKEN, tag);

            if (held.getCount() == 1) {
                player.setHeldItem(hand, BabyChickenBucket);
            } else {
                held.shrink(1);
                PlayerHelper.addOrDropStack(player, BabyChickenBucket);
            }

            player.swingArm(hand);
            entity.remove();
        }
    }
}
