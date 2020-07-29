package svenhjol.meson.helper;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class WorldHelper {
    public static Biome getBiome(ServerWorld world, BlockPos pos) {
        BiomeManager biomeManager = world.getWorldServer().getBiomeManager();
        return biomeManager.getBiome(pos);
    }

    public static ResourceLocation getDimension(World world) {
        RegistryKey<World> key = world.func_234923_W_();
        return key.func_240901_a_();
    }

    public static double getDistanceSq(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static boolean isDimension(World world, ResourceLocation dim) {
        return getDimension(world).equals(dim);
    }

    public static boolean isInsideStructure(ServerWorld world, BlockPos pos, Structure<?> structure) {
        return world.func_241112_a_().func_235010_a_(pos, true, structure).isValid();
    }

    @Nullable
    public static BlockPos locateBiome(Biome biome, ServerWorld world, BlockPos pos) {
        return world.func_241116_a_(biome, pos, 6400, 8);
    }

    @Nullable
    public static BlockPos locateStructure(Structure<?> structure, ServerWorld world, BlockPos pos, int radius) {
        return world.func_241117_a_(structure, pos, radius, true); // not sure what the last param does
    }

    public static boolean stateHasProp(BlockState state, Property<?> prop) {
        ImmutableSet<Property<?>> props = state.getValues().keySet();
        return props.contains(prop);
    }

    public static BlockState stateWith(BlockState state, Property<?> prop) {
        return state.func_235896_a_(prop);
    }
}
