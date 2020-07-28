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

public abstract class MesonModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public boolean hasSubscriptions = false;
    public MesonMod mod;
    public String description = "";

    public boolean test() {
        return true;
    }

    public MesonMod getMod() {
        return mod;
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

    public void onCommonSetup(FMLCommonSetupEvent event) {
        // runs when module has been successfully initialised and is enabled
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        // runs on client when module has been successfully initialised and is enabled
    }

    public void onModConfig(ModConfig.ModConfigEvent event) {
        // modules can be enabled/disabled when config changes
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        // do final things
    }

    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        // server-side event just after play selected world is clicked
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        // server-side event, access to resource manager etc.
    }

    public void onServerStarted(FMLServerStartedEvent event) {
        // server-side event, access to resource manager etc.
    }
}
