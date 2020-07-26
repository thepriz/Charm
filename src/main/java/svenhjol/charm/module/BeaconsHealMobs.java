package svenhjol.charm.module;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.List;

public class BeaconsHealMobs extends MesonModule {
    @Module(description = "Passive and friendly mobs will heal themselves within range of a beacon with the regeneration effect.")
    public BeaconsHealMobs() {}

    public static void healInBeaconRange(World world, int levels, BlockPos pos, Effect primaryEffect, Effect secondaryEffect) {
        if (!world.isRemote) {
            double d0 = levels * 10 + 10;
            AxisAlignedBB bb = (new AxisAlignedBB(pos)).grow(d0).expand(0.0D, world.getHeight(), 0.0D);

            if (primaryEffect == Effects.REGENERATION || secondaryEffect == Effects.REGENERATION) {
                List<AnimalEntity> list = world.getEntitiesWithinAABB(AnimalEntity.class, bb);
                list.forEach(animal -> animal.addPotionEffect(new EffectInstance(Effects.REGENERATION, 4 * 20, 1)));
            }
        }
    }
}
