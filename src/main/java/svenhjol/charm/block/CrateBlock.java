package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import svenhjol.charm.module.Crates;
import svenhjol.charm.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

import javax.annotation.Nullable;
import java.util.List;

public class CrateBlock extends MesonBlock {
    private static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
    private static final ResourceLocation CONTENTS = new ResourceLocation("contents");
    private IStorageMaterial type;

    public CrateBlock(MesonModule module, IStorageMaterial type) {
        super(module, type.getString() + "_crate", Block.Properties
            .create(Material.WOOD)
            .sound(SoundType.WOOD)
            .hardnessAndResistance(1.5F));

        this.type = type;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        CrateTileEntity crate = new CrateTileEntity();

        // TODO this might be a shit way to set display names, please check
        crate.setCustomName(new TranslationTextComponent("block." + this.module.mod.getId() + "." + type.getLowercaseName() + "_crate"));

        return crate;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getBurnTime() {
        return 300;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                ((CrateTileEntity)tile).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof CrateTileEntity) {
            CrateTileEntity crate = (CrateTileEntity)tile;
            if (!worldIn.isRemote && player.isCreative()) {
                ItemStack stack = new ItemStack(getBlockByMaterial(this.type));
                CompoundNBT tag = crate.write(new CompoundNBT());
                if (!tag.isEmpty())
                    stack.setTagInfo(BLOCK_ENTITY_TAG, tag);

                if (crate.hasCustomName())
                    stack.setDisplayName(crate.getCustomName());

                ItemEntity entity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                entity.setDefaultPickupDelay();
                worldIn.addEntity(entity);
            } else {
                crate.fillWithLoot(player);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof CrateTileEntity) {
            CrateTileEntity crate = (CrateTileEntity)tile;
            builder = builder.withDynamicDrop(CONTENTS, (context, consumer) -> {
                for (int i = 0; i < crate.getSizeInventory(); i++) {
                    consumer.accept(crate.getStackInSlot(i));
                }
            });
        }
        return super.getDrops(state, builder);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                CrateTileEntity crate = (CrateTileEntity)tile;
                crate.fillWithLoot(player);
                player.openContainer(crate);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CrateTileEntity)
                worldIn.updateComparatorOutputLevel(pos, state.getBlock());

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(pos));
    }


    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        ItemStack stack = super.getItem(worldIn, pos, state);
        CrateTileEntity tile = (CrateTileEntity)worldIn.getTileEntity(pos);
        if (tile == null)
            return ItemStack.EMPTY;

        CompoundNBT tag = tile.write(new CompoundNBT());
        if (!tag.isEmpty())
            stack.setTagInfo(BLOCK_ENTITY_TAG, tag);

        return stack;
    }

    private static Block getBlockByMaterial(IStorageMaterial type) {
        return Crates.CRATE_BLOCKS.get(type);
    }
}
