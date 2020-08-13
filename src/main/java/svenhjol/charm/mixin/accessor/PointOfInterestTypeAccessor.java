package svenhjol.charm.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.village.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeAccessor  {
    @Accessor("BLOCKS_OF_INTEREST")
    static Set<BlockState> getBlocksOfInterest() {
        throw new IllegalStateException();
    }

    @Accessor("POIT_BY_BLOCKSTATE")
    static Map<BlockState, PointOfInterestType> getPoitByBlockState() {
        throw new IllegalStateException();
    }

    @Accessor()
    Set<BlockState> getBlockStates();
}
