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
import svenhjol.charm.Charm;
import svenhjol.charm.render.*;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IMesonEnum;
import svenhjol.meson.helper.ModHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.*;

@SuppressWarnings("unchecked")
public class VariantAnimalTextures extends MesonModule {
    private static final String PREFIX = "textures/entity/";

    public static List<ResourceLocation> wolves = new ArrayList<>();
    public static List<ResourceLocation> cows = new ArrayList<>();
    public static List<ResourceLocation> squids = new ArrayList<>();
    public static List<ResourceLocation> chickens = new ArrayList<>();
    public static List<ResourceLocation> pigs = new ArrayList<>();

    public static List<ResourceLocation> rareWolves = new ArrayList<>();
    public static List<ResourceLocation> rareCows = new ArrayList<>();
    public static List<ResourceLocation> rareSquids = new ArrayList<>();
    public static List<ResourceLocation> rareChickens = new ArrayList<>();
    public static List<ResourceLocation> rarePigs = new ArrayList<>();

    public static Map<ResourceLocation, ResourceLocation> tameWolves = new HashMap<>();
    public static Map<ResourceLocation, ResourceLocation> angryWolves = new HashMap<>();

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
    public VariantAnimalTextures() { }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // add vanilla textures
        wolves.add(new ResourceLocation(PREFIX + "wolf/wolf.png"));
        cows.add(new ResourceLocation(PREFIX + "cow/cow.png"));
        squids.add(new ResourceLocation(PREFIX + "squid/squid.png"));
        chickens.add(new ResourceLocation(PREFIX + "chicken.png"));
        pigs.add(new ResourceLocation(PREFIX + "pig/pig.png"));


        addCharmTextures(wolves, MobType.WOLF, "brownwolf", "greywolf", "blackwolf", "amotwolf", "jupiter1390");

        for (int i = 1; i <= 25; i++)
            addCharmTextures(wolves, MobType.WOLF, "nlg_wolf" + i); // add NeverLoseGuy wolf textures

        for (int i = 1; i <= 1; i++)
            addCharmTextures(rareWolves, MobType.WOLF, "rare_wolf" + i);


        for (int i = 1; i <= 7; i++)
            addCharmTextures(cows, MobType.COW, "cow" + i);

        for (int i = 1; i <= 1; i++)
            addCharmTextures(rareCows, MobType.COW, "rare_cow" + i);


        for (int i = 1; i <= 4; i++)
            addCharmTextures(squids, MobType.SQUID, "squid" + i);

        for (int i = 1; i <= 1; i++)
            addCharmTextures(rareSquids, MobType.SQUID, "rare_squid" + i);


        for (int i = 1; i <= 3; i++)
            addCharmTextures(chickens, MobType.CHICKEN, "chicken" + i);

        for (int i = 1; i <= 2; i++)
            addCharmTextures(rareChickens, MobType.CHICKEN, "rare_chicken" + i);


        for (int i = 1; i <= 2; i++)
            addCharmTextures(pigs, MobType.PIG, "pig" + i);

        for (int i = 1; i <= 1; i++)
            addCharmTextures(rarePigs, MobType.PIG, "rare_pig" + i);
    }

    public enum MobType implements IMesonEnum { WOLF, COW, PIG, CHICKEN, SQUID }

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
        ResourceLocation res = getRandomTexture(entity, wolves, rareWolves);

        if (entity.isTamed()) {
            res = tameWolves.get(res);
        } else if (entity.func_233678_J__()) {
            res = angryWolves.get(res);
        }

        return res;
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getCowTexture(CowEntity entity) {
        return getRandomTexture(entity, cows, rareCows);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getSquidTexture(SquidEntity entity) {
        return getRandomTexture(entity, squids, rareSquids);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getChickenTexture(ChickenEntity entity) {
        return getRandomTexture(entity, chickens, rareChickens);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getPigTexture(PigEntity entity) {
        return getRandomTexture(entity, pigs, rarePigs);
    }

    public static ResourceLocation getRandomTexture(Entity entity, List<ResourceLocation> normalSet, List<ResourceLocation> rareSet) {
        UUID id = entity.getUniqueID();
        boolean isRare = rareVariants && !rareSet.isEmpty() && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % 50 == 0;

        List<ResourceLocation> set = isRare ? rareSet : normalSet;
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    public void addCharmTextures(List<ResourceLocation> set, MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            ResourceLocation res = createResource(type, texture);
            set.add(res);

            if (type == MobType.WOLF) {
                tameWolves.put(res, createResource(type, texture + "_tame"));
                angryWolves.put(res, createResource(type, texture + "_angry"));
            }
        });
    }

    private ResourceLocation createResource(MobType type, String texture) {
        return new ResourceLocation(Charm.MOD_ID, PREFIX + type.getString() + "/" + texture + ".png");
    }
}
