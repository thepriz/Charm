package svenhjol.meson.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
            return false;
        }
        return true;
    }

    public static void changeDimension(ServerPlayerEntity player, ResourceLocation dimension) {
        ServerWorld world = (ServerWorld)player.world;

        if (!WorldHelper.isDimension(world, dimension)) {
            RegistryKey<World> key = WorldHelper.getDimension(dimension);
            ServerWorld toDimension = world.getServer().getWorld(key);

            if (toDimension != null)
                player.func_241206_a_(toDimension);
        }
    }

    public static void damageHeldItem(PlayerEntity player, Hand hand, ItemStack stack, int damage) {
        stack.damageItem(damage, player, (p) -> player.sendBreakAnimation(hand));
    }

    public static ImmutableList<NonNullList<ItemStack>> getInventories(PlayerEntity player) {
        PlayerInventory inventory = player.inventory;
        return ImmutableList.of(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory);
    }

    public static void doLightningNearPlayer(PlayerEntity player) {
        int dist = 24;
        World world = player.world;
        Random rand = world.rand;

        if (player.world.isRemote)
            return;

        if (!WorldHelper.canSeeSky(world, getPosition(player)))
            return;

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        BlockPos pos = player.func_233580_cy_().add(-(dist / 2) + rand.nextInt(dist), 0, -(dist / 2) + rand.nextInt(dist));

        WorldHelper.doLightning(world, pos, (ServerPlayerEntity)player);
    }

    public static BlockPos getPosition(PlayerEntity player) {
        return player.func_233580_cy_();
    }

    public static boolean isCrouching(PlayerEntity player) {
        return player.isCrouching();
    }

    public static void setHeldItem(PlayerEntity player, Hand hand, ItemStack item) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() == 1) {
            player.setHeldItem(hand, item);
        } else {
            stack.shrink(1);
            if (stack.getCount() == 0) {
                player.setHeldItem(hand, item);
            } else {
                addOrDropStack(player, item);
            }
        }
    }

    public static boolean spawnEntityNearPlayer(PlayerEntity player, MobEntity mob, BiConsumer<MobEntity, BlockPos> onSpawn) {
        boolean spawned = false;
        int range = 8;
        int tries = 8;
        BlockPos pp = player.func_233580_cy_();
        World world = player.world;
        Random rand = world.rand;
        List<BlockPos> valid = new ArrayList<>();

        for (int y = pp.getY() + range * 2; y > 0; y--) {
            for (int i = range; i > 1; i--) {
                for (int c = 1; c < tries; c++) {
                    BlockPos p = new BlockPos(pp.getX() + rand.nextInt(i), y, pp.getZ() + rand.nextInt(i));
                    BlockPos floor = p.down();
                    BlockPos above = p.up();

                    boolean areaIsValid = (WorldHelper.isSolidishBlock(world, floor))
                        && (WorldHelper.isAirBlock(world, p))
                        && (WorldHelper.isAirBlock(world, p));

                    if (areaIsValid) valid.add(above);
                    if (valid.size() > 2) break;
                }
            }
        }

        if (valid.isEmpty()) return false;

        BlockPos spawnPos = valid.get(rand.nextInt(valid.size()));
        mob.moveToBlockPosAndAngles(spawnPos, 0.0F, 0.0F);
        mob.onInitialSpawn(world, world.getDifficultyForLocation(spawnPos), SpawnReason.TRIGGERED, null, null);
        world.addEntity(mob);
        onSpawn.accept(mob, spawnPos);
        return true;
    }

    public static void teleport(PlayerEntity player, BlockPos pos, String dim) {
        teleport(player, pos, dim, p -> {
        });
    }

    public static void teleport(PlayerEntity player, BlockPos pos, String dim, Consumer<PlayerEntity> onTeleport) {
        World world = player.world;
        if (world.isRemote) return;

        PlayerHelper.changeDimension((ServerPlayerEntity)player, new ResourceLocation(dim));

        // ((ServerPlayerEntity)player).teleport((ServerWorld)world, pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);

        BlockPos playerPos = player.func_233580_cy_();

        while (world.isAirBlock(playerPos.down()) && playerPos.getY() > 0) {
            playerPos = playerPos.down();
        }

        while (world.getBlockState(playerPos).isSolid() || world.getBlockState(playerPos.up()).isSolid() && playerPos.getY() < 256) {
            playerPos = playerPos.up();
        }

        player.setPositionAndUpdate(playerPos.getX(), playerPos.getY(), playerPos.getZ());

        onTeleport.accept(player);
    }

    public static void teleportSurface(PlayerEntity player, BlockPos pos, String dim) {
        teleportSurface(player, pos, dim, p -> {
        });
    }

    public static void teleportSurface(PlayerEntity player, BlockPos pos, String dim, Consumer<PlayerEntity> onTeleport) {
        if (player.world.isRemote) return;
        ServerWorld world = (ServerWorld)player.world;

        teleport(player, pos, dim, (p) -> {
            for (int y = p.world.getHeight(); y > 0; y--) {
                BlockPos pp = new BlockPos(p.func_233580_cy_().getX(), y, p.func_233580_cy_().getZ());
                if (p.world.isAirBlock(pp)
                    && !p.world.isAirBlock(pp.down())
                ) {
                    p.setPositionAndUpdate(pp.getX(), y, pp.getZ());
                    onTeleport.accept(p);
                    break;
                }
            }
        });
    }
}
