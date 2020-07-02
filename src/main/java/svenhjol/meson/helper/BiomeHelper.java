package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

public class BiomeHelper {
    public static String getBiomeName(Biome biome) {
        if (biome == null) return "";
        return Objects.requireNonNull(biome.getRegistryName()).getPath();
    }

    public static Biome getBiomeAtPos(ServerWorld world, BlockPos pos) {
        BiomeManager biomeManager = world.getWorldServer().getBiomeManager();
        return biomeManager.getBiome(pos);
    }
}
