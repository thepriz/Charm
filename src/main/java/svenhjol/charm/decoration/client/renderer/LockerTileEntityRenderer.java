package svenhjol.charm.decoration.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.decoration.block.LockerBlock;
import svenhjol.charm.decoration.client.LockersClient;
import svenhjol.charm.decoration.module.Lockers;
import svenhjol.charm.decoration.state.LockerType;
import svenhjol.charm.decoration.tileentity.LockerTileEntity;

public class LockerTileEntityRenderer extends TileEntityRenderer<LockerTileEntity>
{
    private final LockerModel	simpleLocker	= new LockerModel();
    private final LockerModel	largeLocker		= new LargeLockerModel();
    private ItemRenderer		itemRenderer;

    public LockerTileEntityRenderer( TileEntityRendererDispatcher rendererDispatcher )
    {
        super( rendererDispatcher );
    }

    @Override
    public void render( LockerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
                        int combinedOverlay )
    {
        final BlockState blockState = tileEntity.hasWorld() ? tileEntity.getBlockState()
            : Lockers.locker.getDefaultState().with( LockerBlock.FACING, Direction.SOUTH );
        final LockerType lockerType = blockState.has( LockerBlock.TYPE ) ? blockState.get( LockerBlock.TYPE )
            : LockerType.SINGLE;
        final DoorHingeSide hingeSide = blockState.has( BlockStateProperties.DOOR_HINGE ) ? blockState.get( BlockStateProperties.DOOR_HINGE )
            : DoorHingeSide.LEFT;

        if( lockerType != LockerType.TOP )
        {
            final boolean flag = lockerType != LockerType.SINGLE;
            final LockerModel modelLocker = getLockerModel( tileEntity, flag );

            matrixStack.push();

            final float f = blockState.get( LockerBlock.FACING ).getHorizontalAngle();
            matrixStack.translate( 0.5, 0.5, 0.5 );
            matrixStack.rotate( Vector3f.YP.rotationDegrees( -f ) );
            matrixStack.translate( -0.5, -0.5, -0.5 );

            final Material material = getMaterial( tileEntity, flag );
            final IVertexBuilder vertexBuilder = material.getBuffer( buffer, RenderType::getEntityCutout );

            rotateDoor( tileEntity, partialTicks, modelLocker, hingeSide );
            modelLocker.render( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F, hingeSide == DoorHingeSide.LEFT );
            matrixStack.pop();
        }
    }

    private LockerModel getLockerModel( LockerTileEntity tileEntityLocker, boolean flag )
    {
        return flag ? largeLocker : simpleLocker;
    }

    protected Material getMaterial( LockerTileEntity tileEntity, boolean flag )
    {
        final ResourceLocation resourcelocation;
        resourcelocation = flag ? LockersClient.DOUBLE_RESOURCE : LockersClient.SINGLE_RESOURCE;
        return new Material( PlayerContainer.LOCATION_BLOCKS_TEXTURE, resourcelocation );
    }

    private void rotateDoor( LockerTileEntity tileEntityLocker, float partialTicks, LockerModel modelLocker, DoorHingeSide hingeSide )
    {
        float angle = ( (IChestLid)tileEntityLocker ).getLidAngle( partialTicks );
        angle = 1.0F - angle;
        angle = 1.0F - angle * angle * angle;
        modelLocker.rotateDoor( angle, hingeSide == DoorHingeSide.LEFT );
    }
}
