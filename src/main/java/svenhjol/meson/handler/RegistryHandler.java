package svenhjol.meson.handler;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IMesonBlock;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RegistryHandler {

    /**
     * Entry point to register all the things.
     */
    public static void register(String modId, IForgeRegistryEntry<?> obj, ResourceLocation res) {
        if (res == null)
            throw new RuntimeException("Can't register something without a resource location");

        if (obj.getRegistryName() == null)
            obj.setRegistryName(GameData.checkPrefix(res.toString(), false));

        // add to the mod's registry queue for later registration
        Class<?> registryType = obj.getRegistryType();
        Meson.getMod(modId).addToRegistryQueue(registryType, obj);
        Meson.LOG.debug(LogHandler.REGISTRY, "Mod " + modId + " queuing object " + obj.getRegistryType());

        // do custom/additional register actions
        register(modId, registryType, obj, res);
    }

    /**
     * Do additional registry actions here.
     */
    private static void register(String modId, Class<?> registryType, IForgeRegistryEntry<?> obj, ResourceLocation res) {
        if (registryType == Block.class && obj instanceof IMesonBlock) {
            register(modId, ((IMesonBlock)obj).getBlockItem(), res); // also register BlockItem
        }
    }

    /**
     * This method is attached to Forge's event bus by the Meson singleton.
     */
    public static void onRegister(RegistryEvent.Register<?> event) {
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
    }
}
