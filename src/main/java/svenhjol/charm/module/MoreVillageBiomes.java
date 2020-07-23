package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreVillageBiomes extends MesonModule {
    public static List<Biome> plainsBiomes = new ArrayList<>();
    public static List<Biome> taigaBiomes = new ArrayList<>();

    @Module(description = "Villages can spawn in swamps, jungles and bedrock-edition biomes.", hasSubscriptions = true)
    public MoreVillageBiomes() {
        plainsBiomes = Arrays.asList(Biomes.JUNGLE, Biomes.SWAMP, Biomes.SUNFLOWER_PLAINS);
        taigaBiomes = Arrays.asList(Biomes.TAIGA_HILLS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // there isn't dedicated structure pieces for jungles and swamps so just use plains
        plainsBiomes.forEach(biome -> biome.func_235063_a_(DefaultBiomeFeatures.field_235182_t_));
        taigaBiomes.forEach(biome -> biome.func_235063_a_(DefaultBiomeFeatures.field_235186_x_));
    }

    @SubscribeEvent
    public void onVillagerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.isCanceled()) {
            changeVillagerSkin(event.getEntity());
        }
    }

    public void changeVillagerSkin(Entity entity) {
        if (!entity.world.isRemote
            && entity instanceof VillagerEntity
            && entity.addedToChunk
            && entity.ticksExisted == 0
        ) {
            VillagerEntity villager = (VillagerEntity) entity;
            VillagerData data = villager.getVillagerData();

            if (data.getType() == IVillagerType.PLAINS) {
                Biome biome = WorldHelper.getBiome((ServerWorld)entity.world, entity.func_233580_cy_());

                if (plainsBiomes.contains(biome))
                    villager.setVillagerData(data.withType(IVillagerType.byBiome(biome)));
            }
        }
    }
}
