package svenhjol.charm.decoration.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import svenhjol.charm.decoration.state.LockerType;
import svenhjol.charm.decoration.tileentity.LockerTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.helper.PlayerHelper;

import javax.annotation.Nullable;

public class LockerBlock extends MesonBlock implements IWaterLoggable {
    public static final EnumProperty<LockerType> TYPE = EnumProperty.create("type", LockerType.class);
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE_NORTH = makeCuboidShape(0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D);
    protected static final VoxelShape SHAPE_WEST = makeCuboidShape(1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_EAST = makeCuboidShape(0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D);

    public LockerBlock(MesonModule module) {
        super(module, "locker", Block.Properties
            .create(Material.WOOD)
            .hardnessAndResistance(2.5F)
            .sound(SoundType.WOOD));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(TYPE, FACING, HINGE, WATERLOGGED);
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LockerTileEntity();
    }

	/*@Override
	public boolean hasCustomBreakingProgress( BlockState state )
	{
		return true;
	}*/

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
            default:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
        }
    }

    /*
     * =========
     * Placement
     * =========
     */

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        LockerType connectedType = LockerType.SINGLE;
        final Direction direction = context.getPlacementHorizontalFacing().getOpposite();
        final IFluidState fluidState = context.getWorld().getFluidState(context.getPos());
        final boolean sneaking = context.getPlayer() != null && PlayerHelper.isCrouching(context.getPlayer()); // wrapper for both versions of MC

        DoorHingeSide hingeSide = getHingeSide(context);
        if (connectedType == LockerType.SINGLE && !sneaking)
            if (direction == getDirectionToAttach(context, Direction.DOWN)) {
                connectedType = LockerType.TOP;
                hingeSide = context.getWorld().getBlockState(context.getPos().offset(Direction.DOWN)).get(HINGE);
            } else if (direction == getDirectionToAttach(context, Direction.UP)) {
                connectedType = LockerType.BOTTOM;
                hingeSide = context.getWorld().getBlockState(context.getPos().offset(Direction.UP)).get(HINGE);
            }

        return getDefaultState().with(FACING, direction).with(HINGE, hingeSide).with(TYPE, connectedType).with(WATERLOGGED,
            fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
                                          BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED))
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));

        if (facingState.getBlock() == this && facing.getAxis().isVertical()) {
            final LockerType lockerType = facingState.get(TYPE);

            if (stateIn.get(TYPE) == LockerType.SINGLE && lockerType != LockerType.SINGLE
                && stateIn.get(FACING) == facingState.get(FACING) && getDirectionToAttached(facingState) == facing.getOpposite())
                return stateIn.with(TYPE, lockerType.opposite());
        } else if (getDirectionToAttached(stateIn) == facing)
            return stateIn.with(TYPE, LockerType.SINGLE);

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    private DoorHingeSide getHingeSide(BlockItemUseContext context) {
        final BlockPos blockPos = context.getPos();
        final Direction direction = context.getPlacementHorizontalFacing();
        final int offX = direction.getXOffset();
        final int offY = direction.getZOffset();
        final Vec3d v = context.getHitVec();
        final double hitX = v.x - blockPos.getX();
        final double hitY = v.z - blockPos.getZ();
        return (offX >= 0 || !(hitY < 0.5D)) && (offX <= 0 || !(hitY > 0.5D)) && (offY >= 0 || !(hitX > 0.5D))
            && (offY <= 0 || !(hitX < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
    }

    @Nullable
    private Direction getDirectionToAttach(BlockItemUseContext context, Direction facing) {
        final BlockState blockState = context.getWorld().getBlockState(context.getPos());
        final BlockState faceState = context.getWorld().getBlockState(context.getPos().offset(facing));
        return faceState.getBlock() == this && faceState.get(TYPE) == LockerType.SINGLE ? faceState.get(FACING) : null;
    }

    public static Direction getDirectionToAttached(BlockState state) {
        return state.get(TYPE) == LockerType.TOP ? Direction.DOWN : Direction.UP;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof LockerTileEntity)
                ((LockerTileEntity) tileentity).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof LockerTileEntity) {
//                ((LockerTileEntity) tileentity).dropInventoryItems();
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    /*
     * ===========
     * Adjustments
     * ===========
     */

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return calcRedstoneFromInventory(
            ((LockerTileEntity) worldIn.getTileEntity(pos)).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY));
    }

    public static int calcRedstoneFromInventory(@Nullable LazyOptional<IItemHandler> lazyOptional) {
        if (!lazyOptional.isPresent())
            return 0;
        else {
            final IItemHandler inventory = lazyOptional.orElseGet(null);
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < inventory.getSlots(); ++j) {
                final ItemStack itemstack = inventory.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / itemstack.getMaxStackSize();
                    ++i;
                }
            }

            f = f / inventory.getSlots();
            return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }

    /*
     * ===========
     * Interaction
     * ===========
     */

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (hit.getFace() == state.get(FACING)) {
            if (worldIn.isRemote) {
                return ActionResultType.PASS;
            } else {
                final INamedContainerProvider locker = getContainer(state, worldIn, pos);
                if (locker != null) {
                    player.openContainer(locker);
                    player.addStat(getOpenStat());
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return getContainer(state, worldIn, pos, false);
    }

    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos, boolean allowBlockedChest) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof LockerTileEntity))
            return null;
        else if (!allowBlockedChest && isBlocked(worldIn, pos))
            return null;
        else {
            final LockerTileEntity locker = (LockerTileEntity) tileentity;
            final LockerType chesttype = state.get(TYPE);
            if (chesttype == LockerType.SINGLE)
                return locker;
            else {
                final BlockPos blockpos = pos.offset(getDirectionToAttached(state));
                final BlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == this) {
                    final LockerType chesttype1 = iblockstate.get(TYPE);
                    if (chesttype1 != LockerType.SINGLE && chesttype != chesttype1 && iblockstate.get(FACING) == state.get(FACING))
                        if (!allowBlockedChest && isBlocked(worldIn, blockpos))
                            return null;
                }

                return locker;
            }
        }
    }

    private static boolean isBlocked(IWorld world, BlockPos pos) {
        return isBehindSolidBlock(world, pos);
    }

    private static boolean isBehindSolidBlock(IBlockReader reader, BlockPos worldIn) {
        final Direction facing = reader.getBlockState(worldIn).get(FACING);
        final BlockPos blockpos = worldIn.offset(facing);
        return reader.getBlockState(blockpos).isNormalCube(reader, blockpos);
    }

    public static <T> T getLockerInventory(BlockState state, IWorld world, BlockPos pos, boolean allowBlocked, InventoryFactory<T> factory) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (!(tileentity instanceof LockerTileEntity)) {
            return (T) null;
        } else if (!allowBlocked && isBlocked(world, pos)) {
            return (T) null;
        } else {
            LockerTileEntity chesttileentity = (LockerTileEntity) tileentity;
            LockerType chesttype = state.get(TYPE);
            if (chesttype == LockerType.SINGLE) {
                return factory.forSingle(chesttileentity);
            } else {
                BlockPos blockpos = pos.offset(getDirectionToAttached(state));
                BlockState blockstate = world.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock()) {
                    LockerType chesttype1 = blockstate.get(TYPE);
                    if (chesttype1 != LockerType.SINGLE && chesttype != chesttype1 && blockstate.get(FACING) == state.get(FACING)) {
                        if (!allowBlocked && isBlocked(world, blockpos)) {
                            return (T) null;
                        }

                        TileEntity tileentity1 = world.getTileEntity(blockpos);
                        if (tileentity1 instanceof LockerTileEntity) {
                            LockerTileEntity chesttileentity1 = chesttype == LockerType.TOP ? chesttileentity : (LockerTileEntity) tileentity1;
                            LockerTileEntity chesttileentity2 = chesttype == LockerType.TOP ? (LockerTileEntity) tileentity1 : chesttileentity;
                            return factory.forDouble(chesttileentity1, chesttileentity2);
                        }
                    }
                }

                return factory.forSingle(chesttileentity);
            }
        }
    }

    /*
     * =====
     * Fluid
     * =====
     */

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    interface InventoryFactory<T> {
        T forDouble(LockerTileEntity tile1, LockerTileEntity tile2);

        T forSingle(LockerTileEntity tile1);
    }
}
