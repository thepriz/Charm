package svenhjol.charm.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.item.MesonItem;

public class BabyChickenBucketItem extends MesonItem {
    public static final String STORED_BABY_CHICKEN = "stored_baby_chicken";

    public BabyChickenBucketItem(MesonModule module) {
        super(module, "baby_chicken_bucket", new Item.Properties()
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

        world.playSound(null, player.getPosition(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

        if (!player.isCreative()) {
            // spawn the chicken
            ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
            CompoundNBT data = ItemNBTHelper.getCompound(held, STORED_BABY_CHICKEN);
            if (!data.isEmpty())
                chicken.read(data);

            chicken.setPosition(x, y, z);
            world.addEntity(chicken);

            // damage the chicken :(
            chicken.setHealth(chicken.getHealth() - 1.0F);
        }
        player.swingArm(hand);

        if (!player.isCreative())
            player.setHeldItem(hand, new ItemStack(Items.BUCKET));

        return ActionResultType.SUCCESS;
    }
}
