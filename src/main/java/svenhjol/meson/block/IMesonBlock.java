package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import svenhjol.charm.mixin.accessor.FireBlockAccessor;
import svenhjol.meson.MesonModule;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface IMesonBlock {
    ItemGroup getItemGroup();

    boolean enabled();

    default int getMaxStackSize() {
        return 64;
    }

    default int getBurnTime() { return 0; }

    default void register(MesonModule module, String name) {
        module.getMod().register((Block)this, new ResourceLocation(module.getMod().getId(), name));
    }

    default BlockItem createBlockItem() {
        Item.Properties props = new Item.Properties();

        // set up custom props for the blockitem
        ItemGroup group = getItemGroup();
        if (group != null) props.group(group);

        props.maxStackSize(getMaxStackSize());

        // set item stack renderer function if present
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Supplier<Callable<ItemStackTileEntityRenderer>> ister = this.getISTER();

            if (ister != null)
                props.setISTER(ister);
        });

        return new MesonBlockItem(this, props);
    }

    default void setBlockItem(BlockItem blockItem) {
        // no op
    }

    default void setFireInfo(int encouragement, int flammability) {
        ((FireBlockAccessor) Blocks.FIRE).invokeSetFireInfo((Block)this, encouragement, flammability);
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    default Supplier<Callable<ItemStackTileEntityRenderer>> getISTER() { return null; }

    @Nullable
    default BiConsumer<ItemStack, Boolean> getInventoryTickConsumer() {
        return null;
    }
}
