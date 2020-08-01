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
import svenhjol.meson.iface.Module;

public class InventoryCrafting extends MesonModule {
    private static final ITextComponent LABEL = new TranslationTextComponent("container.crafting");
    public static InventoryCraftingClient client;

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
