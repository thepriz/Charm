package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
    @Invoker()
    void callSetLeftShoulderEntity(CompoundNBT tag);

    @Invoker()
    void callSetRightShoulderEntity(CompoundNBT tag);

    @Invoker()
    void callSpawnShoulderEntity(CompoundNBT tag);
}
