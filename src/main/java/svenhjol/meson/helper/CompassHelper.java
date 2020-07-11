package svenhjol.meson.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompassHelper {
    public static ItemStack getBoundCompass(ResourceLocation dimension, BlockPos pos) {
        ItemStack compass = new ItemStack(Items.COMPASS);
        RegistryKey<World> key = WorldHelper.getDimension(dimension);
        Logger logger = LogManager.getLogger();

        CompoundNBT tag = new CompoundNBT();

        tag.put("LodestonePos", NBTUtil.writeBlockPos(pos));
        World.field_234917_f_.encodeStart(NBTDynamicOps.INSTANCE, key).resultOrPartial(logger::error).ifPresent((d) -> {
            tag.put("LodestoneDimension", d);
        });
        tag.putBoolean("LodestoneTracked", true);

        compass.write(tag);
        return compass;
    }
}
