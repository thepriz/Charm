package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonFallingBlock;
import svenhjol.meson.block.IMesonBlock;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "NullableProblems"})
public class SugarBlock extends MesonFallingBlock implements IMesonBlock {
    public SugarBlock(MesonModule module) {
        super(module, "sugar_block", Properties
            .create(Material.SAND)
            .sound(SoundType.SAND)
            .hardnessAndResistance(0.5F)
            .harvestTool(ToolType.SHOVEL)
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchWater(World world, BlockPos pos, BlockState state) {
        boolean waterBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.offset(facing);
                if (world.getBlockState(below).getMaterial() == Material.WATER) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
            world.removeBlock(pos, true);
        }

        return waterBelow;
    }
}