package svenhjol.charm.decoration.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import svenhjol.charm.decoration.module.Lockers;
import svenhjol.charm.decoration.block.LockerBlock;
import svenhjol.charm.decoration.container.LockerContainer;
import svenhjol.charm.decoration.state.LockerType;

public class LockerTileEntity extends LockableLootTileEntity implements IChestLid, ITickableTileEntity, INamedContainerProvider, INameable {
    protected int ticksSinceSync;
    protected int numPlayersUsing;
    public int ticksExisted = 0;
    public float lidAngle = 0;
    public float prevLidAngle = 0;

    public LockerTileEntity(TileEntityType< ? > tileEntityTypeIn )
    {
        super( tileEntityTypeIn );
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {

    }

    public LockerTileEntity()
    {
        super(Lockers.tile );
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB( pos.add( -1, 0, -1 ), pos.add( 2, 2, 2 ) );
    }

//	@Override
//	protected String getConnectableName()
//	{
//		return Lockers.CONTAINER_LOCKER_NAME;
//	}
//
//	@Override
//	public BlockPos getConnected() {
//		if( isConnected() )
//			if( isMain() )
//				return pos.up();
//			else
//				return pos.down();
//		return null;
//	}

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory) {
        return LockerContainer.instance(id, playerInventory);
//		return new LockerContainer(Lockers.container, windowID, playerInventory, 3);
//		if( isMain() )
//			return new LockerContainer( windowID, playerInventory, world, pos );
//		else
//			return getMainTileEntity().createMenu( windowID, playerInventory, player );
    }

    @Override
    public void tick() {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        ++ticksSinceSync;
        if( !world.isRemote && numPlayersUsing != 0 && ( ticksSinceSync + x + y + z ) % 200 == 0 )
        {
            numPlayersUsing = 0;
            final float f = 5.0F;

            for( final PlayerEntity entityplayer : world.getEntitiesWithinAABB( PlayerEntity.class,
                new AxisAlignedBB( x - 5.0F, y - 5.0F, z - 5.0F, x + 1 + 5.0F, y + 1 + 5.0F, z + 1 + 5.0F ) ) )
                if( entityplayer.openContainer instanceof LockerContainer )
                    ++numPlayersUsing;
        }

        prevLidAngle = lidAngle;
        final float f1 = 0.1F;
        if( numPlayersUsing > 0 && lidAngle == 0.0F )
            playSound( SoundEvents.BLOCK_CHEST_OPEN );

        if( numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F )
        {
            final float f2 = lidAngle;
            if( numPlayersUsing > 0 )
                lidAngle += 0.1F;
            else
                lidAngle -= 0.1F;

            if( lidAngle > 1.0F )
                lidAngle = 1.0F;

            final float f3 = 0.5F;
            if( lidAngle < 0.5F && f2 >= 0.5F )
                playSound( SoundEvents.BLOCK_CHEST_CLOSE );

            if( lidAngle < 0.0F )
                lidAngle = 0.0F;
        }
    }

    protected void playSound(SoundEvent soundIn) {
        final LockerType chesttype = getBlockState().get(LockerBlock.TYPE);
        double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        if( chesttype != LockerType.SINGLE )
        {
            final Direction enumfacing = LockerBlock.getDirectionToAttached( getBlockState() );
            x += enumfacing.getXOffset() * 0.5D;
            z += enumfacing.getZOffset() * 0.5D;
        }

        world.playSound( (PlayerEntity)null, x, y, z, soundIn, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F );
    }

    @Override
    public float getLidAngle( float partialTicks )
    {
        return prevLidAngle + ( lidAngle - prevLidAngle ) * partialTicks;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
