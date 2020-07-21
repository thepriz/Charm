package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.module.*;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;

import java.util.Arrays;
import java.util.List;

@Mod(Charm.MOD_ID)
public class Charm extends MesonMod {
    public static final String MOD_ID = "charm";

    public Charm() {
        super(MOD_ID);
    }

    protected List<Class<? extends MesonModule>> getModules() {
        return Arrays.asList(
            AutomaticRecipeUnlock.class,
            ChickensDropFeathers.class,
            ExtractEnchantments.class,
            HuskImprovements.class,
            LanternImprovements.class,
            MoreVillageBiomes.class,
            NoAnvilMinimumXp.class,
            RemoveNitwits.class,
            RemovePotionGlint.class,
            StackableBooks.class,
            StackablePotions.class,
            UseTotemFromInventory.class,
            VillagersFollowEmeralds.class
        );
    }
}
