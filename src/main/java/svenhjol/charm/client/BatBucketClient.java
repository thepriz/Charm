package svenhjol.charm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BatBucketClient {
    public int ticks;
    public double range;
    public static List<LivingEntity> entities = new ArrayList<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START
            && Minecraft.getInstance().player != null
            && ticks > 0
            && range > 0
        ) {
            PlayerEntity player = Minecraft.getInstance().player;

            // sample nearby entities and set them glowing
            if (ticks % 10 == 0 || entities.isEmpty()) {
                setGlowing(false);
                setNearbyEntities(player);
                setGlowing(true);
            }

            if (--ticks <= 0)
                setGlowing(false);
        }
    }

    private void setNearbyEntities(PlayerEntity player) {
        entities.clear();
        AxisAlignedBB area = player.getBoundingBox().grow(range, range / 2.0, range);
        Predicate<LivingEntity> selector = entity -> true;
        entities = player.world.getEntitiesWithinAABB(LivingEntity.class, area, selector);
    }

    private void setGlowing(boolean glowing) {
        for (Entity entity : entities) {
            entity.setGlowing(glowing);
        }
    }
}
