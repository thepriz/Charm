package svenhjol.charm.mixin;

import net.minecraft.potion.Effect;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.BeaconsHealMobs;
import svenhjol.meson.Meson;

import javax.annotation.Nullable;

@Mixin(BeaconTileEntity.class)
public abstract class BeaconsHealMobsMixin extends TileEntity {
    @Shadow private int levels;

    @Shadow @Nullable private Effect primaryEffect;

    @Shadow @Nullable private Effect secondaryEffect;

    public BeaconsHealMobsMixin(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Inject(
        method = "addEffectsToPlayers",
        at = @At("HEAD")
    )
    private void addEffectsHook(CallbackInfo ci) {
        if (Meson.enabled("charm:beacons_heal_mobs") && this.world != null)
            BeaconsHealMobs.healInBeaconRange(this.world, this.levels, this.pos, this.primaryEffect, this.secondaryEffect);
    }
}
