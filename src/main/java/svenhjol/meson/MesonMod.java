package svenhjol.meson;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import svenhjol.meson.handler.ConfigHandler;
import svenhjol.meson.iface.IForgeLoadEvents;

import java.util.List;
import java.util.function.Consumer;

public abstract class MesonMod implements IForgeLoadEvents {
    private final String id;
    private final ConfigHandler configHandler;

    public MesonMod(String id) {
        this.id = id;
        Meson.INSTANCE.register(this);

        this.configHandler = new ConfigHandler(this, getModules());
        this.configHandler.modules.forEach(MesonModule::init);
        this.configHandler.modules.forEach(MesonModule::initClient);
        this.configHandler.refreshConfig();
    }

    protected abstract List<Class<? extends MesonModule>> getModules();

    public String getId() {
        return id;
    }

    public boolean enabled(String name) {
        return configHandler.enabledModules.containsKey(name);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
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

    @Override
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

    public void eachEnabledModule(Consumer<MesonModule> consumer) {
        configHandler.enabledModules.values().forEach(consumer);
    }
}
