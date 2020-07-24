package svenhjol.charm.gen;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class MineshaftOrePlacement extends SimplePlacement<FrequencyConfig> {
    public MineshaftOrePlacement(Codec<FrequencyConfig> config) {
        super(config);
    }

    @Override
    protected Stream<BlockPos> getPositions(Random rand, FrequencyConfig config, BlockPos pos) {
        List<BlockPos> locations = Lists.newArrayList();

        for (int i = 0; i < rand.nextInt(rand.nextInt(config.count) + 1) + 1; ++i) {
            int x = rand.nextInt(16);
            int y = rand.nextInt(64);
            int z = rand.nextInt(16);
            locations.add(pos.add(x, y, z));
        }

        return locations.stream();
    }
}
