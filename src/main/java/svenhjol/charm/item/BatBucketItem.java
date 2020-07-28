package svenhjol.charm.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.message.ClientSetGlowingEntities;
import svenhjol.charm.module.BatBucket;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.item.MesonItem;

public class BatBucketItem extends MesonItem {
    public static final String STORED_BAT = "stored_bat";

    public BatBucketItem(MesonModule module) {
        super(module, "bat_bucket", new Item.Properties()
            .group(ItemGroup.MISC)
            .maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getPlayer() == null)
            return ActionResultType.FAIL;

        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();
        Hand hand = context.getHand();
        ItemStack held = player.getHeldItem(hand);

        double x = pos.getX() + 0.5F + facing.getXOffset();
        double y = pos.getY() + 0.25F + (world.rand.nextFloat() / 2.0F) + facing.getYOffset();
        double z = pos.getZ() + 0.5F + facing.getZOffset();

        world.playSound(null, player.getPosition(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 1.0F, 1.0F);

        if (!player.isCreative()) {
            // spawn the bat
            BatEntity bat = new BatEntity(EntityType.BAT, world);
            CompoundNBT data = ItemNBTHelper.getCompound(held, STORED_BAT);
            if (!data.isEmpty())
                bat.read(data);

            bat.setPosition(x, y, z);
            world.addEntity(bat);

            // damage the bat :(
            bat.setHealth(bat.getHealth() - 1.0F);
        }
        player.swingArm(hand);

        // send client message to start glowing
        if (!world.isRemote)
            Meson.getMod(Charm.MOD_ID).getPacketHandler().sendToPlayer(new ClientSetGlowingEntities(BatBucket.glowingRange, BatBucket.glowingTime * 20), (ServerPlayerEntity) player);

        if (!player.isCreative())
            player.setHeldItem(hand, new ItemStack(Items.BUCKET));

        return ActionResultType.SUCCESS;
    }
}
