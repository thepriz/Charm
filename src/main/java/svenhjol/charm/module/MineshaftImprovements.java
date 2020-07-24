package svenhjol.charm.module;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.MineshaftPieces;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MineshaftImprovements extends MesonModule {
    @Module(description = "")
    public MineshaftImprovements() {}

    public static void generatePiece(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (piece instanceof MineshaftPieces.Corridor) {
            corridor(piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof MineshaftPieces.Room) {
            room(piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof MineshaftPieces.Stairs) {
            stairs(piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private static void corridor(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        List<BlockState> floor = new ArrayList<>(Arrays.asList(
            Blocks.BARREL.getDefaultState().with(BarrelBlock.PROPERTY_FACING, Direction.UP),
            Blocks.LANTERN.getDefaultState(),
            Blocks.CRAFTING_TABLE.getDefaultState(),
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.COBBLESTONE_SLAB.getDefaultState(),
            Blocks.SMOOTH_STONE.getDefaultState(),
            Blocks.SMOOTH_STONE_SLAB.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState()
        ));

        List<BlockState> ceiling = new ArrayList<>(Arrays.asList(
            Blocks.COBWEB.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true)
        ));

        for (int i = 0; i < rand.nextInt(10); i++) {
            int x = rand.nextInt(3);
            int y = 2;
            int z = rand.nextInt(6);

            if (validCeilingBlock(piece, world, x, y, z, box))
                piece.randomlyPlaceBlock(world, box, rand, 0.5F, x, y, z, ceiling.get(rand.nextInt(ceiling.size())));
        }

        for (int i = 0; i < rand.nextInt(10); i++) {
            int x = rand.nextInt(2) == 0 ? 0 : 2;
            int y = 0;
            int z = rand.nextInt(6);

            if (validFloorBlock(piece, world, x, y, z, box))
                piece.randomlyPlaceBlock(world, box, rand, 0.6F, x, y, z, floor.get(rand.nextInt(floor.size())));
        }
    }

    private static void room(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // todo
    }

    private static void stairs(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // todo
    }

    private static boolean validCeilingBlock(StructurePiece piece, IWorld world, int x, int y, int z, MutableBoundingBox box) {
        BlockPos blockpos = new BlockPos(piece.getXWithOffset(x, z), piece.getYWithOffset(y), piece.getZWithOffset(x, z));
        return box.isVecInside(blockpos) && world.getBlockState(blockpos.up()).isSolid();
    }

    private static boolean validFloorBlock(StructurePiece piece, IWorld world, int x, int y, int z, MutableBoundingBox box) {
        BlockPos blockpos = new BlockPos(piece.getXWithOffset(x, z), piece.getYWithOffset(y), piece.getZWithOffset(x, z));
        return box.isVecInside(blockpos) && world.getBlockState(blockpos.down()).isSolid() && world.isAirBlock(blockpos.up());
    }
}
