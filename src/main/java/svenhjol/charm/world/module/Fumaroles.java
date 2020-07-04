package svenhjol.charm.world.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.block.FumaroleBlock;
import svenhjol.charm.world.gen.placement.FumarolePlacement;
import svenhjol.charm.world.gen.placer.FumarolePlacer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import static net.minecraft.block.Blocks.NETHERRACK;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Fumaroles are small columns of hot steam rising from the nether floor.\n" +
        "Sometimes they erupt, sending entities that are placed on them high into the air.")
public class Fumaroles extends MesonModule {
    public static FumaroleBlock block;
    public static BlockClusterFeatureConfig feature = null;
    public static Placement<FrequencyConfig> placement = null;

    @Config(name = "Eruption volume", description = "Volume of the eruption. 1.0 is full volume, 0.0 silent.")
    public static double eruptionVolume = 0.2D;

    @Config(name = "Boost elytra", description = "If true, flying over a fumarole will give the player a vertical boost.")
    public static boolean elytraBoost = true;

    @Override
    public void init() {
        block = new FumaroleBlock(this);
        BlockState state = block.getDefaultState();

        feature = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), FumarolePlacer.placer)).tries(64).whitelist(ImmutableSet.of(NETHERRACK.getBlock())).func_227317_b_().build();
        placement = (new FumarolePlacement(FrequencyConfig.field_236971_a_));

        ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "fumarole");
        RegistryHandler.registerPlacement(placement, ID);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // Nether Wastes biome
        Biomes.field_235254_j_.addFeature(Decoration.UNDERGROUND_DECORATION,
            Feature.RANDOM_PATCH
                .withConfiguration(feature)
                .withPlacement(placement.configure(new FrequencyConfig(4))));
    }

    // This uses code from Quark's CampfiresBoostElytra
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (!elytraBoost)
            return;

        PlayerEntity player = event.player;

        if (player.isElytraFlying()) {
            Vector3d motion = player.getMotion();
            if (motion.getY() < 1) {
                BlockPos pos = PlayerHelper.getPosition(player);
                World world = player.world;

                int moves = 0;
                while (world.isAirBlock(pos) && pos.getY() > 0 && moves < 20) {
                    pos = pos.down();
                    moves++;
                }

                BlockState state = world.getBlockState(pos);
                if (state.getBlock() == block) {
                    double force = 0.5;
                    if (moves > 16)
                        force -= (force * (1.0 - ((double) moves - 16.0) / 4.0));

                    player.setMotion(motion.getX(), Math.min(1, motion.getY() + force), motion.getZ());
                }
            }
        }
    }
}
