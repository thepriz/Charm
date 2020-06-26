package svenhjol.charm.world.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.world.module.EndPortalRunes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RunePortalTileEntity extends TileEntity {
    public BlockPos portal;
    public List<Integer> colors = new ArrayList<>();

    public RunePortalTileEntity() {
        super(EndPortalRunes.tile);
    }

    // TODO: was #read
    @Override
    public void func_230337_a_(BlockState state, CompoundNBT tag) {
        super.func_230337_a_(state, tag);
        loadFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        return writeToNBT(nbt);
    }

    protected void loadFromNBT(CompoundNBT tag) {
        this.portal = BlockPos.fromLong(tag.getLong("portal"));
        this.colors = Arrays.stream(tag.getIntArray("colors")).boxed().collect(Collectors.toList());
    }

    protected CompoundNBT writeToNBT(CompoundNBT tag) {
        if (portal != null) {
            tag.putLong("portal", portal.toLong());
        }
        if (colors != null) {
            tag.putIntArray("colors", colors.stream().mapToInt(i -> i).toArray());
        }
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);

        CompoundNBT c = pkt.getNbtCompound();
        BlockPos blockpos = new BlockPos(c.getInt("x"), c.getInt("y"), c.getInt("z"));

        if (this.world != null) {
            BlockState state = this.world.getBlockState(blockpos);
            handleUpdateTag(state, pkt.getNbtCompound());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderFace(Direction face) {
        return face == Direction.UP;
    }
}
