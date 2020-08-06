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
    private static List<String> TEXTURE_HAS_SUBDIRS = new ArrayList<>();

    public static List<String> wolf = new ArrayList<>();
    public static List<String> cow = new ArrayList<>();
    public static List<String> squid = new ArrayList<>();
    public static List<String> chicken = new ArrayList<>();
    public static List<String> pig = new ArrayList<>();

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

    @Config(name = "Override", description = "Some animal textures are disabled if Quark is present. Set true to force enable them.")
    public static boolean override = false;

    @Module(description = "Animals may spawn with different textures.")
    public VariantAnimalTextures() {
        TEXTURE_HAS_SUBDIRS = new ArrayList<>(Arrays.asList("cow", "pig", "wolf"));
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // wolf textures
        wolf.addAll(Arrays.asList(
            "minecraft:wolf", "charm:brownwolf", "charm:greywolf", "charm:blackwolf", "charm:amotwolf", "charm:jupiter1390"
        ));

        // add NeverLoseGuy wolf textures
        for (int i = 1; i <= 25; i++) {
            wolf.add("charm:wolf" + i);
        }

        // cow textures. Corianthes: add the names of the textures in textures/entity/cow here, without the .png
        cow.addAll(Arrays.asList(
            "minecraft:cow"
        ));

        // squid textures. Corianthes: add the names of the textures in textures/entity/squid here, without the .png
        squid.addAll(Arrays.asList(
            "minecraft:squid", "charm:squid1", "charm:squid2", "charm:squid3", "charm:squid6", "charm:squid7"
        ));

        // chicken textures. Corianthes: add the names of the textures in textures/entity/chicken here, without the .png
        chicken.addAll(Arrays.asList(
            "minecraft:chicken"
        ));

        // pig textures. Corianthes: add the names of the textures in textures/entity/pig here, without the .png
        pig.addAll(Arrays.asList(
            "minecraft:pig"
        ));
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
        String texture = getRandomTexture(entity, wolf);
        if (entity.isTamed()) {
            texture += "_tame";
        } else if (entity.func_233678_J__()) {
            texture += "_angry";
        }

        return getTextureFromString(MobType.WOLF, texture);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getCowTexture(CowEntity entity) {
        return getTextureFromString(MobType.COW, getRandomTexture(entity, cow));
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getSquidTexture(SquidEntity entity) {
        return getTextureFromString(MobType.SQUID, getRandomTexture(entity, squid));
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getChickenTexture(ChickenEntity entity) {
        return getTextureFromString(MobType.CHICKEN, getRandomTexture(entity, chicken));
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getPigTexture(PigEntity entity) {
        return getTextureFromString(MobType.PIG, getRandomTexture(entity, pig));
    }

    public static String getRandomTexture(Entity entity, List<String> set) {
        UUID id = entity.getUniqueID();
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    public static ResourceLocation getTextureFromString(MobType type, String texture) {
        String typeName = type.getLowercaseName();
        String[] a = texture.split(":");

        String mod = a[0].toLowerCase();
        String file = a[1].toLowerCase();
        String prefix = "textures/entity/";

        if (mod.equals("minecraft") && TEXTURE_HAS_SUBDIRS.contains(typeName))
            prefix += typeName + "/";

        return new ResourceLocation(mod, prefix + file + ".png");
    }
}
