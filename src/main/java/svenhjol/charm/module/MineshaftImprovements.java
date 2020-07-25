package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.MineshaftPieces.Corridor;
import net.minecraft.world.gen.feature.structure.MineshaftPieces.Room;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MineshaftImprovements extends MesonModule {
    public static List<BlockState> commonFloorBlocks = new ArrayList<>();
    public static List<BlockState> commonCeilingBlocks = new ArrayList<>();
    public static List<BlockState> rareFloorBlocks = new ArrayList<>();
    public static List<BlockState> rareCeilngBlocks = new ArrayList<>();
    public static List<BlockState> pileBlocks = new ArrayList<>();
    public static List<BlockState> roomBlocks = new ArrayList<>();
    public static List<BlockState> roomDecoration = new ArrayList<>();

    public static float floorBlockChance = 0.09F;
    public static float ceilingBlockChance = 0.04F;
    public static float rareBlockChance = 0.09F;
    public static float roomBlockChance = 0.40F;
    public static float blockPileChance = 0.14F;

    @Config(name = "Corridor blocks", description = "If true, stone, ore, lanterns and TNT will spawn inside mineshaft corridors.")
    public static boolean generateCorridorBlocks = true;

    @Config(name = "Corridor block piles", description = "If true, occasionally there will be piles of ore in mineshaft corridors.")
    public static boolean generateCorridorPiles = true;

    @Config(name = "Room blocks", description = "If true, precious ores will spawn in the central mineshaft room.")
    public static boolean generateRoomBlocks = true;

    @Module(description = "Adds decoration and more ores to mineshafts.")
    public MineshaftImprovements() { }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        commonFloorBlocks.addAll(Arrays.asList(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.COBBLESTONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.ANDESITE_SLAB.getDefaultState(),
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

        pileBlocks.addAll(Arrays.asList(
            Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.COARSE_DIRT.getDefaultState(),
            Blocks.GRAVEL.getDefaultState()
        ));

        commonCeilingBlocks.addAll(Arrays.asList(
            Blocks.COBWEB.getDefaultState(),
            Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState()
        ));

        rareCeilngBlocks.addAll(Arrays.asList(
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState()
        ));

        roomBlocks.addAll(Arrays.asList(
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState()
        ));

        roomDecoration.addAll(Arrays.asList(
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(),
            Blocks.GRASS.getDefaultState(),
            Blocks.FERN.getDefaultState(),
            Blocks.OAK_SAPLING.getDefaultState()
        ));
    }

    public static void generatePiece(StructurePiece piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (piece instanceof Corridor) {
            corridor((Corridor)piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof Room) {
            room((Room)piece, world, structureManager, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private static void corridor(Corridor piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        int bx = box.maxX - box.minX;
        int bz = box.maxZ - box.minZ;

        if (generateCorridorBlocks) {
            if (bx <= 0) bx = 3;
            if (bz <= 0) bz = 7;
            for (int x = 0; x < bx; x++) {
                if (x == 1 && rand.nextFloat() < 0.08F)
                    continue; // rarely, spawn some block in the middle of the corridor
                for (int z = 0; z < bz; z++) {
                    if (validFloorBlock(piece, world, x, 0, z, box) && rand.nextFloat() < floorBlockChance) {
                        piece.setBlockState(world, getFloorBlock(rand), x, 0, z, box);
                    }
                    if (validCeilingBlock(piece, world, x, 2, z, box) && rand.nextFloat() < ceilingBlockChance) {
                        piece.setBlockState(world, getCeilingBlock(rand), x, 2, z, box);
                    }
                }
            }
        }

        if (generateCorridorPiles) {
            if (rand.nextFloat() < blockPileChance) {
                int z = rand.nextInt(bz);
                if (validFloorBlock(piece, world, 1, 0, z, box)) {

                    // select two block states to render in the pile
                    BlockState block1 = getRandomBlockFromList(pileBlocks, rand);
                    BlockState block2 = getRandomBlockFromList(pileBlocks, rand);

                    for (int iy = 0; iy < 3; iy++) {
                        for (int ix = 0; ix <= 2; ix++) {
                            for (int iz = -1; iz <= 1; iz++) {
                                boolean valid = validFloorBlock(piece, world, ix, iy, iz, box);
                                if (valid && rand.nextFloat() < 0.75F)
                                    piece.setBlockState(world, rand.nextFloat() < 0.5 ? block1 : block2, ix, iy, iz, box);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void room(Room piece, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (generateRoomBlocks) {
            int bx = box.maxX - box.minX;
            int bz = box.maxZ - box.minZ;

            if (bx <= 0) bx = 15;
            if (bz <= 0) bz = 15;

            for (int y = 1; y <= 2; y++) {
                for (int x = 0; x <= bx; x++) {
                    for (int z = 0; z <= bz; z++) {
                        if (rand.nextFloat() < roomBlockChance) {
                            BlockState state;
                            if (y == 1) {
                                state = rand.nextFloat() < 0.5F
                                    ? getRandomBlockFromList(roomBlocks, rand)
                                    : getRandomBlockFromList(roomDecoration, rand);
                            } else {
                                if (rand.nextFloat() < 0.5F) continue;
                                state = getRandomBlockFromList(roomBlocks, rand);
                            }
                            BlockPos pos = new BlockPos(piece.boundingBox.minX + x, piece.boundingBox.minY + y, piece.boundingBox.minZ + z);

                            if (world.isAirBlock(pos) && world.getBlockState(pos.down()).isOpaqueCube(world, pos.down()))
                                world.setBlockState(pos, state, 11);
                        }
                    }
                }
            }
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

        boolean vecInside = box.isVecInside(blockpos);
        boolean solidBelow = world.getBlockState(blockpos.down()).isSolid();
        boolean notSlabBelow = !(world.getBlockState(blockpos.down()).getBlock() instanceof SlabBlock);
        boolean airAbove = world.isAirBlock(blockpos.up());
        return vecInside
            && solidBelow
            && notSlabBelow
            && airAbove;
    }

    private static BlockState getFloorBlock(Random rand) {
        return rand.nextFloat() < rareBlockChance
            ? getRandomBlockFromList(rareFloorBlocks, rand)
            : getRandomBlockFromList(commonFloorBlocks, rand);
    }

    private static BlockState getCeilingBlock(Random rand) {
        return rand.nextFloat() < rareBlockChance
            ? getRandomBlockFromList(rareCeilngBlocks, rand)
            : getRandomBlockFromList(commonCeilingBlocks, rand);
    }

    private static BlockState getRandomBlockFromList(List<BlockState> blocks, Random rand) {
        return blocks.get(rand.nextInt(blocks.size()));
    }
}
