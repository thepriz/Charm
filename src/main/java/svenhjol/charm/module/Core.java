package svenhjol.charm.module;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.client.PlayerTickClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class Core extends MesonModule {
    @OnlyIn(Dist.CLIENT)
    public static PlayerTickClient playerTick;

    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Config(name = "Inventory button return", description = "If inventory crafting or inventory ender chest modules are enabled, pressing escape or inventory key returns you to the inventory rather than closing the window.")
    public static boolean inventoryButtonReturn = true;

    @Module(description = "Core configuration values.", alwaysEnabled = true, hasSubscriptions = true)
    public Core() { }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        playerTick = new PlayerTickClient();
        MinecraftForge.EVENT_BUS.register(playerTick);
    }
}
