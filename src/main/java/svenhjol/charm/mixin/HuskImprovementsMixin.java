package svenhjol.charm.mixin;

import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.meson.Meson;

@Mixin(HuskEntity.class)
public class HuskImprovementsMixin {
    @Redirect(
        method = "func_223334_b", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/IWorld;canSeeSky(Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean spawnCheckHook(IWorld world, BlockPos pos) {
        return Meson.enabled("charm:husk_improvements") || world.canSeeSky(pos);
    }
}
