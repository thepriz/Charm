package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import svenhjol.meson.iface.IMesonBlock;

public class RegistryHandler {
    private static final Marker REGISTRY = MarkerManager.getMarker("REGISTRY");

    public static void register(IForgeRegistryEntry<?> obj, ResourceLocation res) {
        Class<?> type = obj.getRegistryType();

        if (type == Block.class && obj instanceof IMesonBlock) {
            // also register BlockItem
            register(((IMesonBlock)obj).getBlockItem(), res);
        }
    }

    public static void onRegister(RegistryEvent.Register<?> event) {

    }
}
