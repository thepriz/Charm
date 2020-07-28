package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.client.BatBucketClient;
import svenhjol.charm.item.BatBucketItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class BatBucket extends MesonModule {
    public static BatBucketItem BAT_BUCKET_ITEM;
    public static BatBucketClient client = null;

    @Config(name = "Glowing time", description = "Number of seconds that entities will receive the glowing effect.")
    public static int glowingTime = 10;

    @Config(name = "Viewing range", description = "Range (in blocks) in which entities will glow.")
    public static int glowingRange = 24;

    @Module(description = "Right-click a bat with a bucket to capture it. Right-click again to release it and locate entities around you.", hasSubscriptions = true)
    public BatBucket() {}

    @Override
    public void init() {
        BAT_BUCKET_ITEM = new BatBucketItem(this);
    }

    @Override
    public void initClient() {
        client = new BatBucketClient();
        MinecraftForge.EVENT_BUS.register(client);
    }

    @SubscribeEvent
    public void onBatInteract(EntityInteract event) {
        if (!event.isCanceled())
            capture(event.getTarget(), event.getPlayer(), event.getHand());
    }

    private void capture(Entity entity, PlayerEntity player, Hand hand) {
        if (!entity.world.isRemote
            && entity instanceof BatEntity
            && ((BatEntity)entity).getHealth() > 0
        ) {
            BatEntity bat = (BatEntity)entity;
            ItemStack held = player.getHeldItem(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET)
                return;

            ItemStack batBucket = new ItemStack(BAT_BUCKET_ITEM);
            CompoundNBT tag = bat.serializeNBT();
            ItemNBTHelper.setCompound(batBucket, BatBucketItem.STORED_BAT, tag);

            if (held.getCount() == 1) {
                player.setHeldItem(hand, batBucket);
            } else {
                held.shrink(1);
                PlayerHelper.addOrDropStack(player, batBucket);
            }

            player.swingArm(hand);
            entity.remove();
        }
    }
}
