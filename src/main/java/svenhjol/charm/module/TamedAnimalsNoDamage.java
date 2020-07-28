package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class TamedAnimalsNoDamage extends MesonModule {
    @Module(description = "Tamed animals no longer take direct damage from players.", hasSubscriptions = true)
    public TamedAnimalsNoDamage() { }

    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        if (!event.isCanceled()
            && event.getTarget() instanceof TameableEntity
            && event.getEntity() instanceof PlayerEntity
            && ((TameableEntity) event.getTarget()).isTamed()
            && !((PlayerEntity) event.getEntity()).isCreative()
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!event.isCanceled()
            && (!(event.getEntityLiving() instanceof PlayerEntity))
        ) {
            Entity attacker = event.getSource().getImmediateSource();
            Entity source = event.getSource().getTrueSource();
            PlayerEntity player = null;

            if (source instanceof PlayerEntity) player = (PlayerEntity) source;
            if (attacker instanceof PlayerEntity) player = (PlayerEntity) attacker;

            if (player != null && !player.isCreative()) {
                LivingEntity target = event.getEntityLiving();
                
                if (target instanceof TameableEntity && ((TameableEntity) target).isTamed())
                    event.setCanceled(true);
            }
        }
    }
}
