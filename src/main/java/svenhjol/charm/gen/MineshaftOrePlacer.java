package svenhjol.charm.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import svenhjol.meson.helper.WorldHelper;

import java.util.Random;

public class MineshaftOrePlacer extends BlockPlacer {
    public static final MineshaftOrePlacer placer = new MineshaftOrePlacer();
    public static final Codec<MineshaftOrePlacer> codec = Codec.unit(() -> placer);

    @Override
    protected BlockPlacerType<?> func_230368_a_() {
        return BlockPlacerType.SIMPLE_BLOCK;
    }

    @Override
    public void func_225567_a_(IWorld worldIn, BlockPos pos, BlockState state, Random rand) {
        for (int i = 0; i < 64; ++i) {
            BlockPos p = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn instanceof ServerWorld && WorldHelper.isInsideStructure((ServerWorld)worldIn, p, Structure.field_236367_c_)) {
                worldIn.setBlockState(p, state, 2);
            }
        }
    }
}
