package svenhjol.charm.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.entity.EndermitePowderEntity;
import svenhjol.charm.item.EndermitePowderItem;
import svenhjol.charm.render.EndermitePowderRenderer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collection;

public class EndermitePowder extends MesonModule {
    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "endermite_powder");
    public static EntityType<EndermitePowderEntity> ENTITY;
    public static EndermitePowderItem ENDERMITE_POWDER;
    public static double lootingBoost = 0.025D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of an endermite dropping endermite powder when killed by the player.")
    public static double dropChance = 0.5D;

    @Module(description = "Endermites drop endermite powder that can be used to locate an End City.", hasSubscriptions = true)
    public EndermitePowder() {}

    @Override
    public void init() {
        ENDERMITE_POWDER = new EndermitePowderItem(this);
        ENTITY = EntityType.Builder.<EndermitePowderEntity>create(EndermitePowderEntity::new, EntityClassification.MISC)
            .setTrackingRange(80)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false)
            .size(2.0F, 2.0F)
            .build(ID.getPath());

        mod.register(ENTITY, ID);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ENTITY, EndermitePowderRenderer::new);
    }

    @SubscribeEvent
    public void onEndermiteDrops(LivingDropsEvent event) {
        if (!event.isCanceled())
            tryDropEndermitePowder(event.getEntityLiving(), event.getDrops(), event.getLootingLevel(), event.getSource());
    }

    private void tryDropEndermitePowder(LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel, DamageSource damageSource) {
        if (!entity.world.isRemote
            && entity instanceof EndermiteEntity
            && damageSource.getTrueSource() instanceof PlayerEntity
            && entity.world.rand.nextFloat() <= dropChance + (lootingBoost * lootingLevel)
        ) {
            EndermiteEntity endermite = (EndermiteEntity) entity;
            ItemStack stack = new ItemStack(ENDERMITE_POWDER);
            BlockPos pos = endermite.getPosition();
            drops.add(new ItemEntity(endermite.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), stack));
        }
    }
}
