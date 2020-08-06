package svenhjol.charm.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {
    @Accessor()
    MutableBoundingBox getBoundingBox();

    @Invoker
    void callRandomlyPlaceBlock(IWorld worldIn, MutableBoundingBox boundingboxIn, Random rand, float chance, int x, int y, int z, BlockState blockstateIn);

    @Invoker
    void callSetBlockState(IWorld worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingboxIn);

    @Invoker
    int callGetXWithOffset(int x, int z);

    @Invoker
    int callGetYWithOffset(int y);

    @Invoker
    int callGetZWithOffset(int x, int z);
}
