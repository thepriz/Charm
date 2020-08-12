package svenhjol.meson.handler;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("unused")
public class ConfigHandler {
    private final List<Runnable> refreshAllConfig = new ArrayList<>();
    private final List<Runnable> refreshSetupConfig = new ArrayList<>();
    private final MesonMod mod;
    public final ImmutableList<MesonModule> modules;
    public final Map<String, MesonModule> enabledModules = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public ConfigHandler(MesonMod mod, List<Class<? extends MesonModule>> classes) {
        this.mod = mod;
        List<MesonModule> moduleInstances = new ArrayList<>();

        // instantiate module, add annotations to module properties
        classes.forEach(clazz -> {
            List<Constructor<?>> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredConstructors()));
            fields.forEach(field -> {
                Module annotation = field.getDeclaredAnnotation(Module.class);
                try {
                    MesonModule module = clazz.getDeclaredConstructor().newInstance();
                    module.mod = mod;
                    module.description = annotation.description();
                    module.alwaysEnabled = annotation.alwaysEnabled();
                    module.enabledByDefault = annotation.enabledByDefault();
                    module.hasSubscriptions = annotation.hasSubscriptions();
                    moduleInstances.add(module);
                } catch (Exception e) {
                    throw new RuntimeException("Could not initialize module class: " + clazz.toString());
                }
            });
        });

        this.modules = ImmutableList.copyOf(moduleInstances);

        // build config tree for modules
        ForgeConfigSpec spec = new ForgeConfigSpec.Builder().configure((b -> buildConfig(b, modules))).getRight();

        // register this mod's config
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        ModConfig config = new ModConfig(ModConfig.Type.COMMON, spec, container);
        container.addConfig(config);

        // config is loaded too late to do vanilla overrides, parse it here
        this.earlyConfigHack(config, modules);
        this.refreshConfig();
    }

    public void refreshConfig() {
        refreshAllConfig.forEach(Runnable::run);
    }

    public void refreshSetupConfig() {
        refreshSetupConfig.forEach(Runnable::run);
    }

    private Void buildConfig(ForgeConfigSpec.Builder builder, ImmutableList<MesonModule> modules) {
        modules.forEach(module -> {
            if (!module.description.isEmpty())
                builder.comment(module.description);

            if (module.alwaysEnabled) {
                updateModule(module);
                return;
            }

            ForgeConfigSpec.ConfigValue<Boolean> val = builder.define(
                module.getName() + " enabled", module.enabledByDefault
            );

            refreshAllConfig.add(() -> {
                module.enabled = module.enabled && val.get() && module.depends();
                updateModule(module);
            });

            refreshSetupConfig.add(() -> {
                module.enabled = module.enabled && module.shouldSetup();
                updateModule(module);
            });
        });

        modules.forEach(module -> {
            builder.push(module.getName());

            List<Field> fields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
            fields.forEach(field -> {
                Config annotation = field.getDeclaredAnnotation(Config.class);
                if (annotation == null)
                    return;

                field.setAccessible(true);
                String name = annotation.name();
                String description = annotation.description();

                if (name.isEmpty())
                    name = field.getName();

                if (!description.isEmpty())
                    builder.comment(description);

                try {
                    ForgeConfigSpec.ConfigValue<?> val;
                    Object defaultVal = field.get(null);

                    if (defaultVal instanceof List) {
                        val = builder.defineList(name, (List<?>) defaultVal, o -> true);
                    } else {
                        val = builder.define(name, defaultVal);
                    }
                    refreshAllConfig.add(() -> {
                        try {
                            field.set(null, val.get());
                        } catch (IllegalAccessException e) {
                            Meson.LOG.error("Could not set config value for " + module.getName());
                            throw new RuntimeException(e);
                        }
                    });

                } catch (ReflectiveOperationException e) {
                    Meson.LOG.error("Failed to get config for " + module.getName());
                }
            });
            builder.pop();
        });
        return null;
    }

    private void earlyConfigHack(ModConfig config, ImmutableList<MesonModule> modules) {
        List<String> lines;

        Path path = FMLPaths.CONFIGDIR.get();
        if (path == null) {
            Meson.LOG.warn("Could not fetch config dir path");
            return;
        }

        String name = config.getFileName();
        if (name == null) {
            Meson.LOG.warn("Could not fetch mod config filename");
            return;
        }

        Path configPath = Paths.get(path.toString() + File.separator + name);
        if (Files.isRegularFile(path)) {
            Meson.LOG.warn("Config file does not exist: " + path);
            return;
        }

        try {
            lines = Files.readAllLines(configPath);
            for (String line : lines) {
                if (!line.contains("enabled")) continue;
                modules.forEach(module -> {
                    if (line.contains(module.getName())) {
                        if (line.contains("false")) {
                            module.enabled = false;
                        } else if (line.contains("true")) {
                            module.enabled = true;
                        }
                    }
                });
            }
        } catch (Exception e) {
            Meson.LOG.warn("Could not read config file: " + e);
        }
    }

    private void updateModule(MesonModule module) {
        if (module.enabled && !enabledModules.containsValue(module)) {
            enabledModules.put(module.getName(), module);
        } else if (!module.enabled) {
            enabledModules.remove(module.getName());
        }
    }
}
