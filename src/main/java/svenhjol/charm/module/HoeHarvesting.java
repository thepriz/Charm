package svenhjol.charm.module;

import com.mojang.brigadier.StringReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;

public class HoeHarvesting extends MesonModule {
    private static List<BlockState> harvestable = new ArrayList<>();

    @Module(description = "", hasSubscriptions = true)
    public HoeHarvesting() {}

    @Override
    public void init() {
        addHarvestable("minecraft:beetroots[age=3]");
        addHarvestable("minecraft:carrots[age=7]");
        addHarvestable("minecraft:nether_wart[age=3]");
        addHarvestable("minecraft:potatoes[age=7]");
        addHarvestable("minecraft:wheat[age=7]");
    }

    public static void addHarvestable(String blockState) {
        BlockState state;

        try {
            BlockStateParser parser = new BlockStateParser(new StringReader(blockState), false).parse(false);
            state = parser.getState();
        } catch (Exception e) {
            state = null;
        }

        if (state == null)
            state = Blocks.AIR.getDefaultState();

        harvestable.add(state);
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled()) {
            boolean result = tryHarvest(event.getEntity(), event.getPos(), event.getHand(), event.getItemStack());
            if (result)
                event.setCanceled(true);
        }
    }

    public boolean tryHarvest(Entity entity, BlockPos pos, Hand hand, ItemStack held) {
        if (!entity.world.isRemote
            && held.getItem() instanceof HoeItem
            && entity instanceof PlayerEntity
        ) {
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            ServerWorld world = (ServerWorld)player.world;
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (!harvestable.contains(state))
                return false;

            Item blockItem = block.asItem();
            BlockState newState = block.getDefaultState();

            List<ItemStack> drops = Block.getDrops(state, world, pos, null, player, ItemStack.EMPTY);
            for (ItemStack drop : drops) {
                if (drop.getItem() == blockItem)
                    drop.shrink(1);

                if (!drop.isEmpty())
                    Block.spawnAsEntity(world, pos, drop);
            }

            world.playEvent(2001, pos, Block.getStateId(newState));
            world.setBlockState(pos, newState);

            // damage the hoe a bit
            held.damageItem(1, player, p -> p.sendBreakAnimation(hand));
            return true;
        }

        return false;
    }
}
