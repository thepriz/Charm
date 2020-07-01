package svenhjol.meson.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class ComposterEvent extends Event {
    public static class Output extends ComposterEvent {
        private World world;
        private BlockPos pos;

        public Output(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        public World getWorld() {
            return world;
        }

        public BlockPos getPos() {
            return pos;
        }
    }
}
