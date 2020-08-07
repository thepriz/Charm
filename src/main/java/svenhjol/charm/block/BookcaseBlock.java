package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import svenhjol.charm.tileentity.BookcaseTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.helper.ModHelper;

import javax.annotation.Nullable;

public class BookcaseBlock extends MesonBlock {
    public static final IntegerProperty SLOTS = IntegerProperty.create("slots", 0, 9);
    public static final BooleanProperty QUARK = BooleanProperty.create("quark");

    private MesonModule module;
    private IStorageMaterial type;
    private boolean quarkEnabled;

    public BookcaseBlock(MesonModule module, IStorageMaterial type) {
        super(module, type.getLowercaseName() + "_bookcase", AbstractBlock.Properties.from(Blocks.BOOKSHELF));

        this.module = module;
        this.type = type;
        this.quarkEnabled = ModHelper.present("quark");

        this.setDefaultState(getDefaultState().with(SLOTS, 0).with(QUARK, quarkEnabled));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isRemote || player.isSpectator())
            return ActionResultType.SUCCESS;

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof BookcaseTileEntity) {
            BookcaseTileEntity bookshelfChest = (BookcaseTileEntity) tile;
            bookshelfChest.fillWithLoot(player);
            player.openContainer(bookshelfChest);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof BookcaseTileEntity) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tile);
                world.updateComparatorOutputLevel(pos, this);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof BookcaseTileEntity) {
                ((BookcaseTileEntity) tile).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return state.get(SLOTS);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        BookcaseTileEntity bookcase = new BookcaseTileEntity();

        // TODO this might be a shit way to set display names, please check
        bookcase.setCustomName(new TranslationTextComponent("block." + this.module.mod.getId() + "." + type.getLowercaseName() + "_bookcase"));

        return bookcase;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 50;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
        return state.get(SLOTS) > 0 ? 1 : 0;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(SLOTS);
        builder.add(QUARK);
    }
}
