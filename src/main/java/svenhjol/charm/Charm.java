package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.modules.AutomaticRecipeUnlockModule;
import svenhjol.charm.modules.NoAnvilMinimumXpModule;
import svenhjol.charm.modules.RemovePotionGlintModule;
import svenhjol.charm.modules.StackablePotionsModule;
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
            AutomaticRecipeUnlockModule.class,
            NoAnvilMinimumXpModule.class,
            RemovePotionGlintModule.class,
            StackablePotionsModule.class
        );
    }
}
