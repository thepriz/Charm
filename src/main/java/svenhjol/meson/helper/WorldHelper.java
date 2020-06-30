package svenhjol.meson.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.meson.iface.IMesonEnum;

@SuppressWarnings("unused")
public class WorldHelper {
    public static final String END_CITY = "EndCity";

    public enum Structure implements IMesonEnum {
        buried_treasure,
        desert_pyramid,
        endcity,
        igloo,
        jungle_pyramid,
        mansion,
        mineshaft,
        fortress,
        monument,
        ocean_ruin,
        pillager_outpost,
        shipwreck,
        stronghold,
        swamp_hut,
        village
    }

    public static BlockRayTraceResult getBlockLookedAt(PlayerEntity player) {
        return getBlockLookedAt(player, 10);
    }

    public static BlockRayTraceResult getBlockLookedAt(PlayerEntity player, int distance) {
        Vector3d vec3d = player.getEyePosition(1.0F);
        Vector3d vec3d1 = player.getLook(1.0F);
        return player.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
    }

    public static boolean canSeeSky(IWorld world, BlockPos pos) {
//        return world.isSkyLightMax(pos); // [1.14]
         return world.canSeeSky(pos); // [1.15]
    }

    public static double getDistanceSq(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static DimensionType getDimension(World world) {
        return world.func_230315_m_();
    }

    public static void clearWeather(ServerWorld world) {
        world.func_241113_a_(
            world.rand.nextInt(12000) + 3600,
            0,
            false,
            false
        );
    }

    public static void stormyWeather(ServerWorld world) {
        world.func_241113_a_(
            0,
            world.rand.nextInt(12000) + 3600,
            true,
            true
        );
    }

    public static boolean isSolidBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        return state.isSolid()
            && !world.isAirBlock(pos)
            && !state.getMaterial().isLiquid();
    }

    public static boolean isSolidishBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        return isSolidBlock(world, pos)
            || state.getMaterial() == Material.LEAVES
            || state.getMaterial() == Material.SNOW
            || state.getMaterial() == Material.ORGANIC
            || state.getMaterial() == Material.PLANTS;
    }

    public static boolean isAirBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        return !state.isSolid()
            || state.getMaterial() == Material.WATER
            || state.getMaterial() == Material.SNOW
            || state.getMaterial() == Material.WATER
            || state.getMaterial() == Material.PLANTS
            || state.getMaterial() == Material.LEAVES
            || state.getMaterial() == Material.ORGANIC;
    }
}
