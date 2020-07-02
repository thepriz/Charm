package svenhjol.charm.world.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Wandering Traders sell Structure Maps that can be used to find additional overworld structures.")
public class WanderingTraderMaps extends MesonModule {
    @Config(name = "Maximum Maps", description = "Maximum number of structure map types a trader may sell.")
    public static int maxMaps = 3;

    @Config(name = "Villager experience awarded", description = "The amount of experience awarded to the trader upon selling.")
    public static int tradeXp = 5;

    @Config(name = "Maximum Trades", description = "Maximum number of times the trade can be used before it locks.")
    public static int maxTrades = 1;

    @Config(name = "General map minimum cost", description = "Minimum emerald cost of a general structure map.")
    public static int generalMinCost = 4;

    @Config(name = "General map maximum cost", description = "Maximum emerald cost of a general structure map.")
    public static int generalMaxCost = 7;

    @Config(name = "Biome-specific map minimum cost", description = "Minimum emerald cost of a biome-specific structure map.")
    public static int biomeMinCost = 16;

    @Config(name = "Biome-specific map maximum cost", description = "Maximum emerald cost of a biome-specific structure map.")
    public static int biomeMaxCost = 22;

    public static List<StructureTrade> trades = new ArrayList<>();

    @Override
    public void init() {
        int bmin = biomeMinCost;
        int bmax = biomeMaxCost;
        int gmin = generalMinCost;
        int gmax = generalMaxCost;

        trades.add(new StructureTrade(Structure.field_236370_f_).setColor(0x866600).setCost(bmin, bmax));
        trades.add(new StructureTrade(Structure.field_236371_g_).setColor(0xA0C0FF).setCost(bmin, bmax));
        trades.add(new StructureTrade(Structure.field_236369_e_).setColor(0x20B020).setCost(bmin, bmax));
        trades.add(new StructureTrade(Structure.field_236367_c_).setColor(0x774400).setCost(gmin, gmax));
        trades.add(new StructureTrade(Structure.field_236377_m_).setColor(0x0000FF).setCost(gmin, gmax));
        trades.add(new StructureTrade(Structure.field_236366_b_).setColor(0xFF0033).setCost(gmin, gmax));
        trades.add(new StructureTrade(Structure.field_236373_i_).setColor(0x990000).setCost(gmin, gmax));
        trades.add(new StructureTrade(Structure.field_236374_j_).setColor(0x107000).setCost(bmin, bmax));
        trades.add(new StructureTrade(Structure.field_236381_q_).setColor(0xCC2200).setCost(gmin, gmax));
        trades.add(new StructureTrade(Structure.field_236372_h_).setColor(0x551155).setCost(gmin, gmax));
    }

    @SubscribeEvent
    public void onWandererTrades(WandererTradesEvent event) {
        List<ITrade> trades = event.getRareTrades();

        for (int i = 0; i < maxMaps; i++) {
            trades.add(new StructureMapForEmeraldsTrade());
        }
    }

    static class StructureMapForEmeraldsTrade implements VillagerTrades.ITrade {
        private final MapDecoration.Type targetType = MapDecoration.Type.TARGET_X;
        private final int maxUses = WanderingTraderMaps.maxTrades;
        private final int tradeXp = WanderingTraderMaps.tradeXp;

        @Nullable
        public MerchantOffer getOffer(Entity merchant, Random rand) {
            StructureTrade structureTrade = trades.get(rand.nextInt(trades.size()));
            World world = merchant.world;

            if (!world.isRemote) {
                ServerWorld serverWorld = (ServerWorld) world;
                BlockPos pos = WorldHelper.findNearestStructure(serverWorld, structureTrade.structure, new BlockPos(merchant.getPositionVec()), 500, true);
                if (pos != null) {

                    // generate the map
                    ItemStack stack = FilledMapItem.setupNewMap(world, pos.getX(), pos.getZ(), (byte) 2, true, true);
                    FilledMapItem.func_226642_a_(serverWorld, stack);
                    MapData.addTargetDecoration(stack, pos, "+", this.targetType);
                    stack.setDisplayName(new TranslationTextComponent("map.charm." + structureTrade.structure.getStructureName().toLowerCase(Locale.ENGLISH)));

                    // set map color based on structure
                    CompoundNBT tag = ItemNBTHelper.getCompound(stack, "display");
                    tag.putInt("MapColor", structureTrade.color);
                    ItemNBTHelper.setCompound(stack, "display", tag);

                    ItemStack in1 = new ItemStack(Items.EMERALD, rand.nextInt(structureTrade.max - structureTrade.min) + structureTrade.min);
                    ItemStack in2 = new ItemStack(Items.COMPASS);
                    float multiplier = 0.2F;
                    return new MerchantOffer(in1, in2, stack, maxUses, tradeXp, multiplier);
                }
            }

            return null;
        }
    }

    public static class StructureTrade {
        public Structure<?> structure;
        public int dimension;
        public int color;
        public int min;
        public int max;

        public StructureTrade(Structure<?> structure) {
            this(structure, 0, 0);
        }

        public StructureTrade(Structure<?> structure, int color, int dimension) {
            this.dimension = dimension;
            this.structure = structure;
            this.color = color;
        }

        public StructureTrade setCost(int min, int max) {
            this.min = min;
            this.max = max;
            return this;
        }

        public StructureTrade setColor(int color) {
            this.color = color;
            return this;
        }
    }
}