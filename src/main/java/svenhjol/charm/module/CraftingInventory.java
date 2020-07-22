package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import svenhjol.charm.client.CraftingInventoryClient;
import svenhjol.charm.container.CraftingInventoryContainer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

public class CraftingInventory extends MesonModule {
    private static final ITextComponent LABEL = new TranslationTextComponent("container.crafting");
    public static CraftingInventoryClient client;

    @Module(description = "Allows crafting from inventory if the player has a crafting table in their inventory.", hasSubscriptions = true)
    public CraftingInventory() {}

    @Override
    public void initClient() {
        client = new CraftingInventoryClient(this.mod);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.openContainer(new SimpleNamedContainerProvider((i, inv, p) -> new CraftingInventoryContainer(i, inv, IWorldPosCallable.of(p.world, p.func_233580_cy_())), LABEL));
    }
}
