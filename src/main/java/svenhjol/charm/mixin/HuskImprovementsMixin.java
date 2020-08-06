package svenhjol.charm.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.HuskImprovements;
import svenhjol.meson.Meson;

import java.util.Random;

@Mixin(HuskEntity.class)
public abstract class HuskImprovementsMixin extends ZombieEntity {
    public HuskImprovementsMixin(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Inject(
        method = "func_223334_b",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void spawnCheckHook(EntityType<HuskEntity> entity, IWorld world, SpawnReason reason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir) {
        if (Meson.enabled("charm:husk_improvements"))
            cir.setReturnValue(canMonsterSpawnInLight(entity, world, reason, pos, rand) && (reason == SpawnReason.SPAWNER || HuskImprovements.canHuskSpawnInLight(world, pos)));
    }
}
