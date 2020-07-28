package svenhjol.charm.module;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import static svenhjol.charm.module.NetheriteNuggets.NETHERITE_NUGGET;

public class DecreaseRepairCost extends MesonModule {
    @Config(name = "XP cost", description = "Number of levels required to reduce repair cost.")
    public static int xpCost = 0;

    @Config(name = "Repair cost decrease", description = "The tool repair cost will be decreased by this number.")
    public static int decreaseAmount = 2;

    @Module(description = "Combine a tool or armor with a netherite nugget on an anvil to reduce its repair cost.", hasSubscriptions = true)
    public DecreaseRepairCost() {}

    @Override
    public boolean test() {
        return Meson.enabled("charm:netherite_nuggets");
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (!Meson.enabled("charm:anvil_improvements"))
            xpCost = 1;
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;

        if (right.getItem() != NETHERITE_NUGGET)
            return;

        if (left.getRepairCost() == 0)
            return;

        int cost;
        cost = left.getRepairCost();
        out = left.copy();
        out.setRepairCost(Math.max(0, cost - decreaseAmount));

        event.setCost(xpCost);
        event.setMaterialCost(1);
        event.setOutput(out);
    }
}
