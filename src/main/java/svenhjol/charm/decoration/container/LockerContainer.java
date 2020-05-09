package svenhjol.charm.decoration.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import svenhjol.charm.decoration.module.Lockers;
import svenhjol.meson.MesonContainer;
import vazkii.quark.api.ITransferManager;
import vazkii.quark.api.QuarkCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LockerContainer extends MesonContainer implements ITransferManager, ICapabilityProvider {
    private final int rows;

    public LockerContainer(ContainerType<?> type, int id, PlayerInventory player, IInventory inventory, int rows) {
        super(type, id, player, inventory);
        this.rows = rows;

        int index = 0;

        for (int j = 0; j < rows; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new LockerSlot(inventory, index++, 8 + (k * 18), 18 + (j * 18)));
            }
        }

        index = 9;

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, index++, 8 + c * 18, 50 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + (i * 18), 108));
        }
    }

    public static LockerContainer instance(int id, PlayerInventory playerInventory, IInventory inventory) {
        return new LockerContainer(Lockers.container, id, playerInventory, inventory, 3); // TODO size
    }

    public static LockerContainer instance(int id, PlayerInventory playerInventory) {
        return instance(id, playerInventory, new Inventory(27)); // TODO size
    }

    @Override
    public boolean acceptsTransfer(PlayerEntity playerEntity) {
        return true;
    }

    @SuppressWarnings("ALL") // what
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return QuarkCapabilities.TRANSFER.orEmpty(cap, LazyOptional.of(() -> this));
    }

    @OnlyIn(Dist.CLIENT)
    public int getNumRows() {
        return this.rows;
    }

//	public LockerContainer(int windowId, PlayerInventory playerInventory, World world, BlockPos pos )
//	{
//		super( Lockers.LOCKER.get(), windowId );
//		tileContainer = (TileEntityContainer)world.getTileEntity( pos );
//		tileContainer.openInventory( playerInventory.player );
//		inventoryContainer = tileContainer.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ).orElse( null );
//
//		if( inventoryContainer instanceof ExpandableStackHandler )
//		{
//			columns = ( (ExpandableStackHandler)inventoryContainer ).getColumns();
//			rows = ( (ExpandableStackHandler)inventoryContainer ).getRows();
//		}
//		else
//		{
//			columns = 9;
//			rows = 3;
//		}
//
//		indexStart = 0;
//		indexHotbar = rows * columns;
//		indexPlayer = indexHotbar + 9;
//
//		for( int i = 0; i < indexHotbar; i++ )
//		{
//			final int x = i % columns * 18 + 8;
//			final int y = i / columns * 18 + 18;
//			addSlot( new SlotItemHandler( inventoryContainer, i, x, y ) );
//		}
//
//		final int offsetX = ( columns - 9 ) / 2 * 18;
//		final int offsetY = 17 + rows * 18;
//
//		for( int i = 0; i < 27; i++ )
//		{
//			final int x = i % 9 * 18 + 8;
//			final int y = 14 + i / 9 * 18;
//			addSlot( new Slot( playerInventory, i + 9, offsetX + x, offsetY + y ) );
//		}
//
//		for( int i = 0; i < 9; i++ )
//		{
//			final int x = i % 9 * 18 + 8;
//			final int y = 72;
//			addSlot( new Slot( playerInventory, i, offsetX + x, offsetY + y ) );
//		}
//	}
//
//	public int getColumns()
//	{
//		return columns;
//	}
//
//	public int getRows()
//	{
//		return rows;
//	}
}
