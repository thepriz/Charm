package svenhjol.charm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.message.ServerSortInventory;
import svenhjol.meson.MesonModule;

import java.util.ArrayList;
import java.util.List;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySortingClient {
    private final MesonModule module;
    private final List<ImageButton> sortingButtons = new ArrayList<>();

    public InventorySortingClient(MesonModule module) {
        this.module = module;
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (!(event.getGui() instanceof ContainerScreen))
            return;

        this.sortingButtons.clear();

        ContainerScreen<?> screen = (ContainerScreen<?>) event.getGui();
        Container container = screen.getContainer();
        List<Slot> slots = container.inventorySlots;

        int x = screen.getGuiLeft() + 160;
        int y = screen.height / 2;

        if (screen instanceof InventoryScreen) {
            this.addSortingButton(screen, x,  y - 42, click -> {
                module.mod.getPacketHandler().sendToServer(new ServerSortInventory(PLAYER));
            });
        } else if (slots.size() > 27) {
            this.addSortingButton(screen, x, y - 80, click -> {
                module.mod.getPacketHandler().sendToServer(new ServerSortInventory(TILE));
            });
            this.addSortingButton(screen, x, y - 14, click -> {
                module.mod.getPacketHandler().sendToServer(new ServerSortInventory(PLAYER));
            });
        } else {
            // shrug
        }

        this.sortingButtons.forEach(event::addWidget);
    }

    private void addSortingButton(ContainerScreen<?> screen, int x, int y, Button.IPressable onPress) {
        this.sortingButtons.add(new ImageButton(x, y, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, onPress));
    }
}
