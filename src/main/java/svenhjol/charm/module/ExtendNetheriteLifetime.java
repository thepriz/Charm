package svenhjol.charm.module;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class ExtendNetheriteLifetime extends MesonModule {
    @Config(name = "Extra lifetime", description = "Additional time (in seconds) given to netherite and netherite-based items before they despawn.")
    public static int extendBy = 300;

    @Module(description = "Extends the lifetime of netherite-based items before they despawn.", hasSubscriptions = true)
    public ExtendNetheriteLifetime() {}

    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        if (!event.isCanceled()) {
            ItemStack stack = event.getEntityItem().getItem();
            Item item = stack.getItem();

            if (stack.isEmpty())
                return;

            if ((item instanceof TieredItem && ((TieredItem)item).getTier() == ItemTier.NETHERITE) || item.isImmuneToFire()) {
                if (event.getEntityItem().getAge() <= 6020) { // 6000 is default lifetime, add a little buffer just in case
                    event.setExtraLife(extendBy * 20); // in ticks
                    event.setCanceled(true);
                }
            }
        }
    }
}
