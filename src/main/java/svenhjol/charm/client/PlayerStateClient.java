package svenhjol.charm.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayerStateClient {
    public boolean mineshaft = false;
    public boolean stronghold = false;
    public boolean fortress = false;
    public boolean shipwreck = false;
    public boolean village = false;
    public boolean bigDungeon = false;
    public boolean isDaytime = true;
}
