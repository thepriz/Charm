package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.MapHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.*;

public class WanderingTraderImprovements extends MesonModule {
    private static final List<WanderingTraderMap> maps = new ArrayList<>();

    @Config(name = "Spawn near signal fire", description = "If true, wandering traders will only spawn if the player is near a signal fire.")
    public static boolean spawnNearSignalFire = true;

    @Config(name = "Trade biome maps", description = "If true, wandering traders will sell maps to biomes.")
    public static boolean tradeBiomeMaps = true;

    @Config(name = "Trade structure maps", description = "If true, wandering traders will sell maps to structures.")
    public static boolean tradeStructureMaps = true;

    @Module(description = "Wandering traders only appear near signal campfires and sell maps to biomes and structures.", hasSubscriptions = true)
    public WanderingTraderImprovements() { }

    @Override
    public void init() {
        if (tradeStructureMaps) {
            maps.addAll(Arrays.asList(
                new StructureMap(Structure.field_236372_h_, false), // ruined portal
                new StructureMap(Structure.field_236381_q_, false), // village
                new StructureMap(Structure.field_236374_j_, false), // swamp hut
                new StructureMap(Structure.field_236373_i_, false), // shipwreck
                new StructureMap(Structure.field_236377_m_, false), // ocean ruin
                new StructureMap(Structure.field_236366_b_, false), // pillager outpost
                new StructureMap(Structure.field_236367_c_, false), // mineshaft
                new StructureMap(Structure.field_236371_g_, false), // igloo
                new StructureMap(Structure.field_236369_e_, true), // jungle temple
                new StructureMap(Structure.field_236370_f_, true) // desert pyramid
            ));
        }

        if (tradeBiomeMaps) {
            maps.addAll(Arrays.asList(
                new BiomeMap(Biomes.WARM_OCEAN, false),
                new BiomeMap(Biomes.SNOWY_TUNDRA, false),
                new BiomeMap(Biomes.DESERT, false),
                new BiomeMap(Biomes.SUNFLOWER_PLAINS, false),
                new BiomeMap(Biomes.FLOWER_FOREST, true),
                new BiomeMap(Biomes.MUSHROOM_FIELDS, true),
                new BiomeMap(Biomes.BAMBOO_JUNGLE, true),
                new BiomeMap(Biomes.ICE_SPIKES, true)
            ));
        }
    }

    @SubscribeEvent
    public void onWandererTrades(WandererTradesEvent event) {
        List<VillagerTrades.ITrade> trades = event.getRareTrades();

        for (int i = 0; i < 3; i++) {
            trades.add(new StructureMapForEmeraldsTrade());
        }
    }

    public static boolean isSignalFireInRange(BlockPos pos) {
        if (!spawnNearSignalFire)
            return true;

        return true; // TODO look for signal fire around pos
    }

    public static class StructureMapForEmeraldsTrade implements VillagerTrades.ITrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            WanderingTraderMap wanderingTraderMap = maps.get(rand.nextInt(maps.size()));

            if (!trader.world.isRemote) {
                ItemStack map = wanderingTraderMap.getMap((ServerWorld) trader.world, new BlockPos(trader.getPositionVec()));
                if (map != null) {
                    ItemStack in1 = new ItemStack(Items.EMERALD, wanderingTraderMap.getCost(rand));
                    ItemStack in2 = new ItemStack(Items.COMPASS);
                    return new MerchantOffer(in1, in2, map, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    public interface WanderingTraderMap {
        @Nullable
        ItemStack getMap(ServerWorld world, BlockPos pos);

        int getCost(Random rand);
    }

    public static class StructureMap implements WanderingTraderMap {
        public Structure<?> structure;
        public boolean rare;

        public StructureMap(Structure<?> structure, boolean rare) {
            this.structure = structure;
            this.rare = rare;
        }

        @Nullable
        @Override
        public ItemStack getMap(ServerWorld world, BlockPos pos) {
            int color = structure.hashCode(); // TODO this is garbage, fixme
            BlockPos nearestStructure = WorldHelper.locateStructure(structure, world, pos, 2000);
            if (nearestStructure == null)
                return null;

            TranslationTextComponent mapName = new TranslationTextComponent("map.charm." + structure.getStructureName().toLowerCase(Locale.ENGLISH));
            return MapHelper.getMap(world, nearestStructure, mapName, MapDecoration.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(4) + 6 : rand.nextInt(2) + 2;
        }
    }

    public static class BiomeMap implements WanderingTraderMap {
        public Biome biome;
        public boolean rare;

        public BiomeMap(Biome biome, boolean rare) {
            this.biome = biome;
            this.rare = rare;
        }

        @Nullable
        @Override
        public ItemStack getMap(ServerWorld world, BlockPos pos) {
            BlockPos nearestBiome = WorldHelper.locateBiome(biome, world, pos);
            if (nearestBiome == null)
                return null;

            ResourceLocation biomeRegName = biome.getRegistryName();
            if (biomeRegName == null)
                return null;

            int color = biome.hashCode(); // TODO this is garbage, fixme

            TranslationTextComponent mapName = new TranslationTextComponent("map.charm." + biomeRegName.getPath().toLowerCase(Locale.ENGLISH));
            return MapHelper.getMap(world, nearestBiome, mapName, MapDecoration.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(3) + 3 : rand.nextInt(1) + 1;
        }
    }
}
