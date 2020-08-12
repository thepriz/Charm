package svenhjol.charm.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.module.VariantChests;
import svenhjol.charm.tileentity.VariantChestTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@SuppressWarnings("NullableProblems")
public class VariantChestBlock extends ChestBlock implements IMesonBlock, IVariantChestBlock {
    private MesonModule module;
    private IStorageMaterial type;

    public VariantChestBlock(MesonModule module, IStorageMaterial type) {
        super(Properties.from(Blocks.CHEST), () -> VariantChests.NORMAL_TILE);

        this.module = module;
        this.type = type;

        this.register(module, type.getLowercaseName() + "_chest");
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
        VariantChestTileEntity chest = new VariantChestTileEntity();

        // TODO this might be a shit way to set display names, please check
        chest.setCustomName(new TranslationTextComponent("block." + this.module.mod.getId() + "." + type.getLowercaseName() + "_chest"));

        return chest;
    }

    @Override
    public IStorageMaterial getMaterialType() {
        return this.type;
    }

    @Override
    public int getBurnTime() {
        return 300;
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public Supplier<Callable<ItemStackTileEntityRenderer>> getISTER() {
        return () -> () -> new ItemStackTileEntityRenderer() {
            private final VariantChestTileEntity tile = new VariantChestTileEntity();

            @Override
            public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
                tile.setMaterialType(getMaterialType());
                TileEntityRendererDispatcher.instance.renderItem(tile, matrix, buffer, combinedLight, combinedOverlay);
            }
        };
    }
}
