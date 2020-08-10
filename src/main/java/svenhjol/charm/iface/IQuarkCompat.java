package svenhjol.charm.iface;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public interface IQuarkCompat {
    @Nullable
    Block getRandomChest(Random rand);

    ItemStack getQuiltedWool(DyeColor color);

    ItemStack getRandomAncientTome(Random rand);

    Structure<?> getBigDungeonStructure();

    boolean hasBigDungeons();

    boolean hasQuiltedWool();

    boolean hasVariantChests();

    boolean isCaveCrystalBlock(World world, BlockPos pos);

    boolean isInsideBigDungeon(ServerWorld world, BlockPos pos);
}
