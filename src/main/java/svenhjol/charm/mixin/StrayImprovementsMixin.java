package svenhjol.charm.mixin;

import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.meson.Meson;

@Mixin(StrayEntity.class)
public class StrayImprovementsMixin {
    @Redirect(
        method = "func_223327_b(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Z",
            at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/IWorld;canSeeSky(Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean spawnCheckHook(IWorld world, BlockPos pos) {
        return Meson.enabled("charm:stray_improvements") || world.canSeeSky(pos);
    }
}
