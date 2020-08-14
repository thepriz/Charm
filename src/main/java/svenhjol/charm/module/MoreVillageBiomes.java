package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.mixin.accessor.BiomeGenerationSettingsAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class MoreVillageBiomes extends MesonModule {
    @Module(description = "Villages can spawn in swamps and jungles.", hasSubscriptions = true)
    public MoreVillageBiomes() { }

    @Override
    public void init() {
        if (enabled) {
            List<RegistryKey<Biome>> biomes = new ArrayList<>(Arrays.asList(
                Biomes.JUNGLE, Biomes.SWAMP
            ));

            for (RegistryKey<Biome> biomeKey : biomes) {
                Biome biome = WorldHelper.getBiomeFromRegistryKey(biomeKey);
                BiomeGenerationSettings settings = biome.func_242440_e();

                // this hack changes the immutablelist to an arraylist so it's possible to add new suppliers.
                // TODO should probably be using the new JSON biomes, I guess?
                List<Supplier<StructureFeature<?, ?>>> existing = ((BiomeGenerationSettingsAccessor) settings).getStructureFeatures();
                ((BiomeGenerationSettingsAccessor)settings).setStructureFeatures(new ArrayList<>(existing));

                ((BiomeGenerationSettingsAccessor)settings).getStructureFeatures().add(() -> StructureFeatures.field_244154_t);
            }
        }
    }

    @SubscribeEvent
    public void onVillagerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.isCanceled())
            changeVillagerSkin(event.getEntity());
    }

    public void changeVillagerSkin(Entity entity) {
        if (!entity.world.isRemote
            && entity instanceof VillagerEntity
            && entity.addedToChunk
            && entity.ticksExisted == 0
        ) {
            VillagerEntity villager = (VillagerEntity) entity;
            VillagerData data = villager.getVillagerData();

            if (data.getType() == VillagerType.PLAINS) {
                Biome biome = WorldHelper.getBiome((ServerWorld)entity.world, entity.getPosition());
                Biome.Category category = biome.getCategory();

                if (category.equals(Biome.Category.JUNGLE) || category.equals(Biome.Category.SWAMP))
                    villager.setVillagerData(data.withType(VillagerType.func_242371_a(WorldHelper.getBiomeKeyAtPosition(villager.world, villager.getPosition()))));
            }
        }
    }
}
