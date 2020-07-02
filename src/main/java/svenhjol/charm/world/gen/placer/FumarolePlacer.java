package svenhjol.charm.world.gen.placer;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import svenhjol.charm.world.module.Fumaroles;

import java.util.Random;

public class FumarolePlacer extends BlockPlacer {
    public static final Codec<FumarolePlacer> codec;
    public static final FumarolePlacer placer = new FumarolePlacer();

    @Override
    protected BlockPlacerType<?> func_230368_a_() {
        return BlockPlacerType.SIMPLE_BLOCK;
    }

    @Override
    public void func_225567_a_(IWorld worldIn, BlockPos pos, BlockState state, Random rand) {
        for (int i = 0; i < 64; ++i) {
            BlockPos p = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(p) && worldIn.getBlockState(p.down()).getBlock() == Blocks.NETHERRACK) {
                worldIn.setBlockState(p.down(), Fumaroles.block.getDefaultState(), 2);
            }
        }
    }

    // copypasta from SimpleBlockPlacer - needed?
    static {
        codec = Codec.unit(() -> placer);
    }
}
