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
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class VariantAnimalTextures extends MesonModule {
    public static List<String> wolf = new ArrayList<>();
    public static List<String> cow = new ArrayList<>();
    public static List<String> squid = new ArrayList<>();
    public static List<String> chicken = new ArrayList<>();
    public static List<String> pig = new ArrayList<>();

    @Module(description = "Animals may spawn with different textures.")
    public VariantAnimalTextures() {}

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
            "minecraft:cow", "charm:cow1", "charm:cow2"
        ));

        // squid textures. Corianthes: add the names of the textures in textures/entity/squid here, without the .png
        squid.addAll(Arrays.asList(
            "minecraft:squid", "charm:squid1", "charm:squid2"
        ));

        // chicken textures. Corianthes: add the names of the textures in textures/entity/chicken here, without the .png
        chicken.addAll(Arrays.asList(
            "minecraft:chicken", "charm:chicken1", "charm:chicken2"
        ));

        // pig textures. Corianthes: add the names of the textures in textures/entity/pig here, without the .png
        pig.addAll(Arrays.asList(
            "minecraft:pig", "charm:pig1", "charm:pig2"
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
        RenderingRegistry.registerEntityRenderingHandler(EntityType.WOLF, VariantWolfRenderer.factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityType.COW, VariantCowRenderer.factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityType.PIG, VariantPigRenderer.factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityType.CHICKEN, VariantChickenRenderer.factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityType.SQUID, VariantSquidRenderer.factory());
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
        String prefix = "textures/entity/" + type.getLowercaseName() + "/";
        String[] a = texture.split(":");
        return new ResourceLocation(a[0], prefix + a[1] + ".png");
    }
}
