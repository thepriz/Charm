package svenhjol.charm.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.StrayImprovements;
import svenhjol.meson.Meson;

import java.util.Random;

@Mixin(StrayEntity.class)
public abstract class StrayImprovementsMixin extends AbstractSkeletonEntity {
    protected StrayImprovementsMixin(EntityType<? extends AbstractSkeletonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Inject(
        method = "func_223327_b",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void spawnCheckHook(EntityType<StrayEntity> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir) {
        if (Meson.enabled("charm:stray_improvements"))
            cir.setReturnValue(canMonsterSpawnInLight(entity, world, reason, pos, rand) && (reason == SpawnReason.SPAWNER || StrayImprovements.canStraySpawnInLight(world, pos)));
    }
}
