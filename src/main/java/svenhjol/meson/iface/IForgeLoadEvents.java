package svenhjol.meson.iface;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public interface IForgeLoadEvents {
    void onCommonSetup(FMLCommonSetupEvent event);
    void onClientSetup(FMLClientSetupEvent event);
    void onModConfig(ModConfig.ModConfigEvent event);
    void onLoadComplete(FMLLoadCompleteEvent event);
    void onServerAboutToStart(FMLServerAboutToStartEvent event);
    void onServerStarting(FMLServerStartingEvent event);
    void onServerStarted(FMLServerStartedEvent event);
}
