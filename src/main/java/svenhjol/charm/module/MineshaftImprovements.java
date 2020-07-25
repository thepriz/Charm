package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.MineshaftPieces.Corridor;
import net.minecraft.world.gen.feature.structure.MineshaftPieces.Cross;
import net.minecraft.world.gen.feature.structure.MineshaftPieces.Room;
import net.minecraft.world.gen.feature.structure.MineshaftPieces.Stairs;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MineshaftImprovements extends MesonModule {
    public static List<BlockState> commonFloorBlocks = new ArrayList<>();
    public static List<BlockState> rareFloorBlocks = new ArrayList<>();
    public static List<BlockState> commonCeilingBlocks = new ArrayList<>();
    public static List<BlockState> rareCeilngBlocks = new ArrayList<>();
    public static List<BlockState> roomBlocks = new ArrayList<>();

    @Module(description = "Adds decoration and more ores to mineshafts.")
    public MineshaftImprovements() { }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        commonFloorBlocks.addAll(Arrays.asList(
            Blocks.GRAVEL.getDefaultState(),
            Blocks.CLAY.getDefaultState(),
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.COBBLESTONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.DIRT.getDefaultState(),
            Blocks.COARSE_DIRT.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState()
        ));

        rareFloorBlocks.addAll(Arrays.asList(
            Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.TNT.getDefaultState(),
            Blocks.LANTERN.getDefaultState()
        ));

        commonCeilingBlocks.addAll(Arrays.asList(
            Blocks.COBWEB.getDefaultState(),
            Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.OAK_PLANKS.getDefaultState()
        ));

        rareCeilngBlocks.addAll(Arrays.asList(
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState()
        ));

        roomBlocks.addAll(Arrays.asList(
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(),
            Blocks.GRASS.getDefaultState(),
            Blocks.FERN.getDefaultState(),
            Blocks.OAK_SAPLING.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState()
        ));
    }

    public static void generatePiece(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (piece instanceof Corridor) {
            corridor((Corridor)piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof Room) {
            room((Room)piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof Stairs) {
            stairs((Stairs)piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof Cross) {
            cross((Cross)piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private static void corridor(Corridor piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        int bx = box.maxX - box.minX;
        int bz = box.maxZ - box.minZ;

        if (bx <= 0) bx = 3;
        if (bz <= 0) bz = 7;

        for (int x = 0; x < bx; x++) {
            if (x == 1) continue;
            for (int z = 0; z < bz; z++) {
                if (validFloorBlock(piece, world, x, 0, z, box) && rand.nextFloat() < 0.1F) {
                    piece.setBlockState(world, getFloorBlock(rand), x, 0, z, box);
                }
                if (validCeilingBlock(piece, world, x, 2, z, box) && rand.nextFloat() < 0.07F) {
                    piece.setBlockState(world, getCeilingBlock(rand), x, 2, z, box);
                }
            }
        }
    }

    private static void room(Room piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        int bx = box.maxX - box.minX;
        int bz = box.maxZ - box.minZ;

        if (bx <= 0) bx = 15;
        if (bz <= 0) bz = 15;

        for (int x = 0; x <= bx; x++) {
            for (int z = 0; z <= bz; z++) {
                if (rand.nextFloat() < 0.42F) {
                    BlockState state = roomBlocks.get(rand.nextInt(roomBlocks.size()));
                    BlockPos pos = new BlockPos(piece.boundingBox.minX + x, piece.boundingBox.minY + 1, piece.boundingBox.minZ + z);

                    if (world.isAirBlock(pos) && world.getBlockState(pos.down()).isSolid())
                        world.setBlockState(pos, state, 11);
                }
            }
        }
    }

    private static void stairs(Stairs piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // not yet
    }

    private static void cross(Cross piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        for (int x = 0; x <= 2; x++) {
            if (rand.nextFloat() < 0.5F) {
                piece.setBlockState(world, getFloorBlock(rand), x, 0, 1, box);
            }
        }

        for (int z = 0; z <= 2; z++) {
            if (rand.nextFloat() < 0.5F) {
                piece.setBlockState(world, getFloorBlock(rand), 1, 0, z, box);
            }
        }

        if (rand.nextFloat() < 0.75F) {
            piece.setBlockState(world, getCeilingBlock(rand), 1, box.maxY - box.minY - 1, 1, box);
        }
    }

    private static boolean validCeilingBlock(StructurePiece piece, IWorld world, int x, int y, int z, MutableBoundingBox box) {
        BlockPos blockpos = new BlockPos(piece.getXWithOffset(x, z), piece.getYWithOffset(y), piece.getZWithOffset(x, z));
        return box.isVecInside(blockpos)
            && world.getBlockState(blockpos.up()).isSolid()
            && world.isAirBlock(blockpos.down());
    }

    private static boolean validFloorBlock(StructurePiece piece, IWorld world, int x, int y, int z, MutableBoundingBox box) {
        BlockPos blockpos = new BlockPos(piece.getXWithOffset(x, z), piece.getYWithOffset(y), piece.getZWithOffset(x, z));
        return box.isVecInside(blockpos)
            && world.getBlockState(blockpos.down()).isSolid()
            && world.isAirBlock(blockpos.up())
            && !world.canSeeSky(blockpos);
    }

    private static BlockState getFloorBlock(Random rand) {
        return rand.nextFloat() < 0.12F
            ? rareFloorBlocks.get(rand.nextInt(rareFloorBlocks.size()))
            : commonFloorBlocks.get(rand.nextInt(commonFloorBlocks.size()));
    }

    private static BlockState getCeilingBlock(Random rand) {
        return rand.nextFloat() < 0.12F
            ? rareCeilngBlocks.get(rand.nextInt(rareCeilngBlocks.size()))
            : commonCeilingBlocks.get(rand.nextInt(commonCeilingBlocks.size()));
    }
}
