package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.render.*;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IMesonEnum;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class VariantAnimalTextures extends MesonModule {
    private static final String PREFIX = "textures/entity/";
    private static List<String> TEXTURE_HAS_SUBDIRS = new ArrayList<>();

    public static List<String> wolves = new ArrayList<>();
    public static List<String> cows = new ArrayList<>();
    public static List<String> squids = new ArrayList<>();
    public static List<String> chickens = new ArrayList<>();
    public static List<String> pigs = new ArrayList<>();

    public static List<String> rareWolves = new ArrayList<>();
    public static List<String> rareCows = new ArrayList<>();
    public static List<String> rareSquids = new ArrayList<>();
    public static List<String> rareChickens = new ArrayList<>();
    public static List<String> rarePigs = new ArrayList<>();

    @Config(name = "Variant wolves", description = "If true, wolves may spawn with different textures.")
    public static boolean variantWolves = true;

    @Config(name = "Variant squids", description = "If true, squids may spawn with different textures.")
    public static boolean variantSquids = true;

    @Config(name = "Variant cows", description = "If true, cows may spawn with different textures. This is disabled if Quark is present.")
    public static boolean variantCows = true;

    @Config(name = "Variant chickens", description = "If true, chickens may spawn with different textures. This is disabled if Quark is present.")
    public static boolean variantChickens = true;

    @Config(name = "Variant pigs", description = "If true, pigs may spawn with different textures. This is disabled if Quark is present.")
    public static boolean variantPigs = true;

    @Config(name = "Rare variants", description = "If true, all animals have a chance to spawn as a rare variant.")
    public static boolean rareVariants = true;

    @Config(name = "Override", description = "Some animal textures are disabled if Quark is present. Set true to force enable them.")
    public static boolean override = false;

    @Module(description = "Animals may spawn with different textures.")
    public VariantAnimalTextures() {
        TEXTURE_HAS_SUBDIRS = new ArrayList<>(Arrays.asList("cow", "pig", "wolf"));
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // add vanilla textures
        wolves.add("minecraft:wolf");
        cows.add("minecraft:cow");
        squids.add("minecraft:squid");
        chickens.add("minecraft:chicken");
        pigs.add("minecraft:pig");

        wolves.addAll(Arrays.asList("charm:brownwolf", "charm:greywolf", "charm:blackwolf", "charm:amotwolf", "charm:jupiter1390"));

        for (int i = 1; i <= 25; i++)
            wolves.add("charm:nlg_wolf" + i); // add NeverLoseGuy wolf textures

        for (int i = 1; i <= 7; i++)
            cows.add("charm:cow" + i);

        for (int i = 1; i <= 4; i++)
            squids.add("charm:squid" + i);

        for (int i = 1; i <= 3; i++)
            chickens.add("charm:chicken" + i);

        for (int i = 1; i <= 2; i++)
            pigs.add("charm:pig" + i);

        // when rares are added
//        for (int i = 1; i <= 1; i++)
//            rareWolves.add("charm:rare_wolf" + i);

        for (int i = 1; i <= 1; i++)
            rareWolves.add("charm:rare_wolf" + i);

        for (int i = 1; i <= 2; i++)
            rareChickens.add("charm:rare_chicken" + i);

        for (int i = 1; i <= 1; i++)
            rareCows.add("charm:rare_cow" + i);

        for (int i = 1; i <= 1; i++)
            rarePigs.add("charm:rare_pig" + i);

        for (int i = 1; i <= 1; i++)
            rareSquids.add("charm:rare_squid" + i);
    }

    public enum MobType implements IMesonEnum {
        WOLF,
        COW,
        PIG,
        CHICKEN,
        SQUID
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        if (variantWolves)
            RenderingRegistry.registerEntityRenderingHandler(EntityType.WOLF, VariantWolfRenderer.factory());

        if (variantSquids)
            RenderingRegistry.registerEntityRenderingHandler(EntityType.SQUID, VariantSquidRenderer.factory());

        if (variantCows && (!ModHelper.present("quark") || override))
            RenderingRegistry.registerEntityRenderingHandler(EntityType.COW, VariantCowRenderer.factory());

        if (variantPigs && (!ModHelper.present("quark") || override))
            RenderingRegistry.registerEntityRenderingHandler(EntityType.PIG, VariantPigRenderer.factory());

        if (variantChickens && (!ModHelper.present("quark") || override))
            RenderingRegistry.registerEntityRenderingHandler(EntityType.CHICKEN, VariantChickenRenderer.factory());
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getWolfTexture(WolfEntity entity) {
        String texture = getRandomTexture(entity, wolves, rareWolves);
        if (entity.isTamed()) {
            texture += "_tame";
        } else if (entity.func_233678_J__()) {
            texture += "_angry";
        }

        return getTextureFromString(MobType.WOLF, texture);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getCowTexture(CowEntity entity) {
        return getTextureFromString(MobType.COW, getRandomTexture(entity, cows, rareCows));
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getSquidTexture(SquidEntity entity) {
        return getTextureFromString(MobType.SQUID, getRandomTexture(entity, squids, rareSquids));
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getChickenTexture(ChickenEntity entity) {
        return getTextureFromString(MobType.CHICKEN, getRandomTexture(entity, chickens, rareChickens));
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getPigTexture(PigEntity entity) {
        return getTextureFromString(MobType.PIG, getRandomTexture(entity, pigs, rarePigs));
    }

    public static String getRandomTexture(Entity entity, List<String> normalSet, List<String> rareSet) {
        UUID id = entity.getUniqueID();
        boolean isRare = rareVariants && !rareSet.isEmpty() && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % 50 == 0;

        List<String> set = isRare ? rareSet : normalSet;
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    public static ResourceLocation getTextureFromString(MobType type, String texture) {
        String typeName = type.getLowercaseName();
        String[] a = texture.split(":");

        String mod = a[0].toLowerCase();
        String file = a[1].toLowerCase();

        // TODO should cache this
        if (!mod.equals("minecraft") || TEXTURE_HAS_SUBDIRS.contains(typeName)) {
            file = PREFIX + typeName + "/" + file + ".png";
        } else {
            file = PREFIX + file + ".png";
        }

        return new ResourceLocation(mod, file);
    }
}
