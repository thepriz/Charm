package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import svenhjol.charm.client.InventoryCraftingClient;
import svenhjol.charm.container.InventoryCraftingContainer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class InventoryCrafting extends MesonModule {
    private static final ITextComponent LABEL = new TranslationTextComponent("container.charm.portable_crafting_table");
    public static InventoryCraftingClient client;

    @Config(name = "Offhand only", description = "If true, the crafting button will only appear when the player holds the crafting table in their offhand.")
    public static boolean offhandOnly = false;

    @Module(description = "Allows crafting from inventory if the player has a crafting table in their inventory.", hasSubscriptions = true)
    public InventoryCrafting() {}

    @Override
    public void initClient() {
        client = new InventoryCraftingClient(this);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.openContainer(new SimpleNamedContainerProvider((i, inv, p) -> new InventoryCraftingContainer(i, inv, IWorldPosCallable.of(p.world, p.getPosition())), LABEL));
    }
}
