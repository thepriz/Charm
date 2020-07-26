package svenhjol.charm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.meson.Meson;

@Mixin(PlayerEntity.class)
public abstract class ParrotsStayOnShoulderMixin extends Entity {

    @Shadow private long timeEntitySatOnShoulder;

    // need to extend Entity so we can access the world prop
    public ParrotsStayOnShoulderMixin(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(
        method = "spawnShoulderEntities",
        at = @At("HEAD"),
        cancellable = true
    )
    private void spawnShoulderEntitiesHook(CallbackInfo ci) {
        if (this.timeEntitySatOnShoulder + 20L < this.world.getGameTime()) {
            if (Meson.enabled("charm:parrots_stay_on_shoulder"))
                ci.cancel();
        }
    }
}
