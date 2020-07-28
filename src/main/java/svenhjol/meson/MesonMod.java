package svenhjol.meson;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.meson.handler.ConfigHandler;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.handler.RegistryHandler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class MesonMod {
    private final String id;
    private final ConfigHandler configHandler;
    private final PacketHandler packetHandler;
    private ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> registryQueue = ArrayListMultimap.create();

    public MesonMod(String id) {
        this.id = id;

        Meson.INSTANCE.register(this);

        this.configHandler = new ConfigHandler(this, getModules());
        this.packetHandler = new PacketHandler(this);

        eachModule(MesonModule::init);
        configHandler.refreshConfig();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eachModule(MesonModule::initClient));
    }

    protected abstract List<Class<? extends MesonModule>> getModules();

    public void register(IForgeRegistryEntry<?> obj, ResourceLocation res) {
        if (res == null)
            throw new RuntimeException("Can't register something without a resource location");

        if (obj.getRegistryName() == null)
            obj.setRegistryName(GameData.checkPrefix(res.toString(), false));

        // add to the mod's registry queue for later registration
        Class<?> registryType = obj.getRegistryType();
        registryQueue.put(registryType, () -> obj);
        Meson.LOG.debug(LogHandler.REGISTRY, "Mod " + getId() + " queuing object " + obj.getRegistryName() + " for " + registryType.getSimpleName().toLowerCase() + " registry");

        // run custom registrations
        RegistryHandler.customRegisters(this, registryType, obj, res);
    }

    public ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> getRegistryQueue() {
        return registryQueue;
    }

    public String getId() {
        return id;
    }

    public boolean enabled(String name) {
        return configHandler.enabledModules.containsKey(name);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        configHandler.refreshConfig();
        configHandler.refreshSetupConfig();

        eachEnabledModule(module -> {
            Meson.LOG.info("Loading module " + module.getName());
            if (module.hasSubscriptions)
                MinecraftForge.EVENT_BUS.register(module);

            module.onCommonSetup(event);
        });
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        eachEnabledModule(module -> module.onClientSetup(event));
    }

    public void onModConfig(ModConfigEvent event) {
        configHandler.refreshSetupConfig();
        eachEnabledModule(module -> module.onModConfig(event));
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        eachEnabledModule(module -> module.onLoadComplete(event));
    }

    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        eachEnabledModule(module -> module.onServerAboutToStart(event));
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        eachEnabledModule(module -> module.onServerStarting(event));
    }

    public void onServerStarted(FMLServerStartedEvent event) {
        eachEnabledModule(module -> module.onServerStarted(event));
    }

    public void eachModule(Consumer<MesonModule> consumer) {
        configHandler.modules.forEach(consumer);
    }

    public void eachEnabledModule(Consumer<MesonModule> consumer) {
        configHandler.enabledModules.values().forEach(consumer);
    }
}
