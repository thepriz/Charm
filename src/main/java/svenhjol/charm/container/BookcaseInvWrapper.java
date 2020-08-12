package svenhjol.charm.container;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class BookcaseInvWrapper extends SidedInvWrapper {
    public BookcaseInvWrapper(ISidedInventory inv, @Nullable Direction side) {
        super(inv, side);
    }

    /** copypasta from {@link net.minecraftforge.items.wrapper.SidedInvWrapper#create} */
    public static LazyOptional<IItemHandlerModifiable>[] create(ISidedInventory inv, Direction... sides) {
        LazyOptional<IItemHandlerModifiable>[] ret = new LazyOptional[sides.length];
        for (int x = 0; x < sides.length; x++) {
            final Direction side = sides[x];
            ret[x] = LazyOptional.of(() -> new BookcaseInvWrapper(inv, side));
        }
        return ret;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        this.inv.markDirty(); // so the TE can update the texture
        return super.extractItem(slot, amount, simulate);
    }
}
