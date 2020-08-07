package svenhjol.charm.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.module.VariantChests;
import svenhjol.charm.tileentity.VariantTrappedChestTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class VariantTrappedChestBlock extends ChestBlock implements IMesonBlock, IVariantChestBlock {
    private MesonModule module;
    private IStorageMaterial type;

    public VariantTrappedChestBlock(MesonModule module, IStorageMaterial type) {
        super(Properties.from(Blocks.TRAPPED_CHEST), () -> VariantChests.TRAPPED_TILE);

        this.module = module;
        this.type = type;

        this.register(module, type.getLowercaseName() + "_trapped_chest");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemGroup(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        VariantTrappedChestTileEntity chest = new VariantTrappedChestTileEntity();

        // TODO this might be a shit way to set display names, please check
        chest.setCustomName(new TranslationTextComponent("block." + this.module.mod.getId() + "." + type.getLowercaseName() + "_trapped_chest"));

        return chest;
    }

    @Override
    public IStorageMaterial getMaterialType() {
        return type;
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public Supplier<Callable<ItemStackTileEntityRenderer>> getISTER() {
        return () -> () -> new ItemStackTileEntityRenderer() {
            private final VariantTrappedChestTileEntity tile = new VariantTrappedChestTileEntity();

            @Override
            public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
                tile.setMaterialType(getMaterialType());
                TileEntityRendererDispatcher.instance.renderItem(tile, matrix, buffer, combinedLight, combinedOverlay);
            }
        };
    }

    //    @Override
//    public void setISTER(Item.Properties props) {
//        props.setISTER(() -> () -> new ItemStackTileEntityRenderer() {
//            private final VariantTrappedChestTileEntity tile = new VariantTrappedChestTileEntity();
//
//            @Override
//            public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
//                tile.setMaterialType(getMaterialType());
//                TileEntityRendererDispatcher.instance.renderItem(tile, matrix, buffer, combinedLight, combinedOverlay);
//            }
//        });
//    }

    /**
     * Copypasta from {@link net.minecraft.block.TrappedChestBlock}
     */

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    public boolean canProvidePower(BlockState state) {
        return true;
    }

    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return MathHelper.clamp(ChestTileEntity.getPlayersUsing(blockAccess, pos), 0, 15);
    }

    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return side == Direction.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }
}
