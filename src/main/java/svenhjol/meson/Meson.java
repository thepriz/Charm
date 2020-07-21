package svenhjol.meson;

import com.google.common.base.CaseFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.handler.RegistryHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Meson {
    public static final String MOD_ID = "Meson";
    public static final Meson INSTANCE = new Meson();
    public static final LogHandler LOG = new LogHandler(MOD_ID);
    private static final Map<String, MesonMod> mods = new ConcurrentHashMap<>();

    private final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    private final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    private Meson() {
        modEventBus.addListener(RegistryHandler::onRegister);
    }

    public void register(MesonMod mod) {
        mods.put(mod.getId(), mod);

        modEventBus.addListener(mod::onCommonSetup);
        modEventBus.addListener(mod::onModConfig);
        modEventBus.addListener(mod::onLoadComplete);
        forgeEventBus.addListener(mod::onServerAboutToStart);
        forgeEventBus.addListener(mod::onServerStarting);
        forgeEventBus.addListener(mod::onServerStarted);
    }

    public static MesonMod getMod(String id) {
        if (mods.containsKey(id)) {
            return mods.get(id);
        } else {
            throw new RuntimeException("No such instance: " + id);
        }
    }

    public static boolean enabled(String res) {
        String[] split = res.split(":");
        String name = split[0];
        String module = split[1];

        if (mods.containsKey(name)) {
            MesonMod mod = mods.get(name);

            if (module.contains("_"))
                module = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, module);

            return mod.enabled(module);
        }

        return false;
    }

    public static boolean enabled(ResourceLocation res) {
        return enabled(res.toString());
    }
}
