package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

public class VariantBookshelfBlock extends MesonBlock {
    public VariantBookshelfBlock(MesonModule module, IStorageMaterial type) {
        super(module, type.getLowercaseName() + "_bookshelf", AbstractBlock.Properties.from(Blocks.BOOKSHELF));

        /** @see net.minecraft.block.FireBlock#init */
        if (type.isFlammable())
            this.setFireInfo(30, 20);
    }

    @Override
    public int getBurnTime() {
        return 300;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
        return 1;
    }
}
