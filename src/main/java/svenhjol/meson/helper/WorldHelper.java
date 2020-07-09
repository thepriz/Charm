package svenhjol.meson.helper;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class WorldHelper {
    public static BlockRayTraceResult getBlockLookedAt(PlayerEntity player) {
        return getBlockLookedAt(player, 10);
    }

    public static BlockRayTraceResult getBlockLookedAt(PlayerEntity player, int distance) {
        Vector3d vec3d = player.getEyePosition(1.0F);
        Vector3d vec3d1 = player.getLook(1.0F);
        return player.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
    }

    public static boolean canSeeSky(IWorld world, BlockPos pos) {
         return world.canSeeSky(pos); // [1.15]
    }

    public static double getDistanceSq(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static boolean doesStateHaveProp(BlockState state, Property<?> prop) {
        ImmutableSet<Property<?>> props = state.getValues().keySet();
        return props.contains(prop);
    }

    public static boolean isDimension(World world, ResourceLocation dim) {
        RegistryKey<World> key = world.func_234923_W_();
        return key.func_240901_a_().equals(dim);
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

    public static void doLightning(World world, BlockPos pos, @Nullable ServerPlayerEntity caster) {
        // copypasta from TridentEntity
        LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        lightning.func_233576_c_(Vector3d.func_237492_c_(pos));
        lightning.setCaster(caster);
        world.addEntity(lightning);
        world.playSound(null, pos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 1.0F, 1.0F);
    }

    @Nullable
    public static BlockPos findNearestStructure(ServerWorld world, ResourceLocation res, BlockPos pos, int radius, boolean dunno) {
        Structure<?> structure = getStructureByResourceLocation(res);
        if (structure == null)
            return null;

        return findNearestStructure(world, structure, pos, radius, dunno);
    }

    public static BlockPos findNearestStructure(ServerWorld world, Structure<?> structure, BlockPos pos, int radius, boolean dunno) {
        return world.func_241117_a_(structure, pos, radius, dunno);
    }

    public static boolean isPositionInsideStructure(ServerWorld world, BlockPos pos, Structure<?> structure) {
        return world.func_241112_a_().func_235010_a_(pos, true, structure).isValid();
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

    @Nullable
    public static Structure<?> getStructureByResourceLocation(ResourceLocation res) {
        return ForgeRegistries.STRUCTURE_FEATURES.getValue(res);
    }
}
