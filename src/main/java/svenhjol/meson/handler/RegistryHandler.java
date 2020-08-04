package svenhjol.meson.handler;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.block.IMesonBlock;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RegistryHandler {

    public static void customRegisters(MesonMod mod, Class<?> registryType, @Nonnull IForgeRegistryEntry<?> obj, @Nonnull ResourceLocation res) {
        if (registryType == Block.class && obj instanceof IMesonBlock)
            mod.getRegistryQueue().put(Item.class, ((IMesonBlock) obj)::getBlockItem);
    }

    @SubscribeEvent
    public static void onRegister(Register<?> event) {
        IForgeRegistry registry = event.getRegistry();
        Class<?> registryType = registry.getRegistrySuperType();

        Meson.getMods().forEach((name, mod) -> {
            ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> queue = mod.getRegistryQueue();
            if (!queue.containsKey(registryType))
                return;

            List<Supplier<IForgeRegistryEntry<?>>> objects = queue.get(registryType);

            objects.forEach(object -> {
                IForgeRegistryEntry<?> entry = object.get();
                if (entry == null) {
                    Meson.LOG.error("Trying to register null");
                    return;
                }

                registry.register(entry);
                Meson.LOG.info(LogHandler.REGISTRY, "Registering to " + registry.getRegistryName() + " - " + entry.getRegistryName());
            });

            queue.removeAll(registryType);
        });

        Meson.LOG.debug("Meson registration has completed.");
    }
}
