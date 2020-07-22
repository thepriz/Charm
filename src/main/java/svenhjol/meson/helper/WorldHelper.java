package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
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

    @Nullable
    public static BlockPos locateBiome(Biome biome, ServerWorld world, BlockPos pos) {
        return world.func_241116_a_(biome, pos, 6400, 8);
    }

    @Nullable
    public static BlockPos locateStructure(Structure<?> structure, ServerWorld world, BlockPos pos, int radius) {
        return world.func_241117_a_(structure, pos, radius, true); // not sure what the last param does
    }
}
