package svenhjol.charm.module;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.client.MusicClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class MusicImprovements extends MesonModule {
    private MusicClient client;

    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;

    @Module(description = "Playing a record in a jukebox stops background music from playing at the same.\n" +
        "Creative music tracks may also play in survival mode.")
    public MusicImprovements() {}

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        client = new MusicClient(this, event);
        MinecraftForge.EVENT_BUS.register(client);
    }
}
