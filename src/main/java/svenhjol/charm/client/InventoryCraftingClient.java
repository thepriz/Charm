package svenhjol.charm.client;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiContainerEvent.DrawForeground;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.message.ServerOpenCrafting;
import svenhjol.meson.MesonModule;

public class InventoryCraftingClient {
    private final MesonModule module;
    public ImageButton craftingButton;

    public InventoryCraftingClient(MesonModule module) {
        this.module = module;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onInitGui(InitGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (!(event.getGui() instanceof InventoryScreen) || event.getGui() instanceof CreativeScreen)
            return;

        InventoryScreen screen = (InventoryScreen)event.getGui();

        this.craftingButton = new ImageButton(screen.getGuiLeft() + 130, screen.height / 2 - 22, 20, 18, 0, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
            this.module.mod.getPacketHandler().sendToServer(new ServerOpenCrafting());
        });

        if (!mc.player.inventory.hasItemStack(new ItemStack(Blocks.CRAFTING_TABLE)))
            craftingButton.visible = false;

        event.addWidget(this.craftingButton);
    }

    @SubscribeEvent
    public void onDrawForeground(DrawForeground event) {
        if (!(event.getGuiContainer() instanceof InventoryScreen))
            return;

        if (craftingButton == null)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if (mc.player.world.getGameTime() % 5 == 0)
            craftingButton.visible = mc.player.inventory.hasItemStack(new ItemStack(Blocks.CRAFTING_TABLE));
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
