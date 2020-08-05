package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import svenhjol.charm.client.InventoryEnderChestClient;
import svenhjol.charm.container.InventoryEnderChestContainer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class InventoryEnderChest extends MesonModule {
    private static ITextComponent LABEL = new TranslationTextComponent("container.enderchest");
    public static InventoryEnderChestClient client;

    @Module(description = "Access Ender Chest contents from the player's inventory.")
    public InventoryEnderChest() {}

    @Override
    public void initClient() {
        client = new InventoryEnderChestClient(this);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.openContainer(new SimpleNamedContainerProvider((i, inv, p) -> new InventoryEnderChestContainer(i, inv, p.getInventoryEnderChest()), LABEL));
    }
}
