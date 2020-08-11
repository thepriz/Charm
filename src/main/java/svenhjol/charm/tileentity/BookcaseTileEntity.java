package svenhjol.charm.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.block.BookcaseBlock;
import svenhjol.charm.container.BookcaseContainer;
import svenhjol.charm.module.Bookcases;
import svenhjol.meson.tileentity.IMesonTileEntity;
import vazkii.quark.api.ITransferManager;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class BookcaseTileEntity extends LockableLootTileEntity implements ICapabilityProvider, IMesonTileEntity, ISidedInventory, ITransferManager {
    public static int SIZE = 9;
    private static final int[] SLOTS = IntStream.range(0, SIZE).toArray();
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public BookcaseTileEntity() {
        super(Bookcases.TILE);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag))
            ItemStackHelper.loadAllItems(tag, this.items);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        if (!this.checkLootAndWrite(tag))
            ItemStackHelper.saveAllItems(tag, this.items);

        return tag;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return BookcaseContainer.instance(id, player, this);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tile.charm.bookcase");
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item) {
        super.setInventorySlotContents(slot, item);
        updateBlockState();
    }

    @Override
    public boolean acceptsTransfer(PlayerEntity player) {
        return true;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        player.world.playSound(null, pos, CharmSounds.BOOKSHELF_OPEN, SoundCategory.BLOCKS, 0.5f, player.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        player.world.playSound(null, pos, CharmSounds.BOOKSHELF_CLOSE, SoundCategory.BLOCKS, 0.5f, player.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    public void updateBlockState() {
        int filled = 0;

        for (int i = 0; i < SIZE; i++) {
            if (!getStackInSlot(i).isEmpty())
                filled++;
        }

        if (world != null && world.getBlockState(pos).getBlock() instanceof BookcaseBlock)
            world.setBlockState(pos, world.getBlockState(pos).with(BookcaseBlock.SLOTS, filled), 2);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return Bookcases.canContainItem(stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    protected IItemHandler createUnSidedHandler() {
        return new SidedInvWrapper(this, Direction.UP);
    }
}
