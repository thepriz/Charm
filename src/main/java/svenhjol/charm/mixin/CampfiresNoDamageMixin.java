package svenhjol.charm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.CampfiresNoDamage;
import svenhjol.meson.Meson;

@Mixin(CampfireBlock.class)
public abstract class CampfiresNoDamageMixin {
    @Inject(
        method = "onEntityCollision",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onEntityCollisionHook(BlockState state, World worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci) {
        if (Meson.enabled("charm:campfires_no_damage")) {
            if (!(CampfiresNoDamage.soulCampfiresDamage && state.getBlock() == Blocks.SOUL_CAMPFIRE))
                ci.cancel();
        }
    }
}
