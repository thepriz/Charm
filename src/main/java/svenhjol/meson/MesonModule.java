package svenhjol.meson;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import svenhjol.meson.iface.IForgeLoadEvents;

public abstract class MesonModule implements IForgeLoadEvents {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public boolean hasSubscriptions = false;
    public MesonMod mod;
    public String description = "";

    public boolean test() {
        return true;
    }

    public String getModId() {
        if (this.mod == null)
            throw new RuntimeException("This meson module has not been set up properly, and will now meet its death with grace.");

        return mod.getId();
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void init() {
        // chance for modules to setup both sides. `enabled` prop available at this point.
    }

    @OnlyIn(Dist.CLIENT)
    public void initClient() {
        // chance for modules to setup client-only things. `enabled` prop available at this point.
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // runs when module has been successfully initialised and is enabled
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        // runs on client when module has been successfully initialised and is enabled
    }

    @Override
    public void onModConfig(ModConfig.ModConfigEvent event) {
        // modules can be enabled/disabled when config changes
    }

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        // do final things
    }

    @Override
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        // server-side event just after play selected world is clicked
    }

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        // server-side event, access to resource manager etc.
    }

    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        // server-side event, access to resource manager etc.
    }
}
