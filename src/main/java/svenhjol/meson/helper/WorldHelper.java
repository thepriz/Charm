package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.server.ServerWorld;

public class WorldHelper {
    public static Biome getBiome(ServerWorld world, BlockPos pos) {
        BiomeManager biomeManager = world.getWorldServer().getBiomeManager();
        return biomeManager.getBiome(pos);
    }
}
