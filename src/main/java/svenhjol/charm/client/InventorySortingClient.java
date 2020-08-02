package svenhjol.charm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.HopperScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.gui.CrateScreen;
import svenhjol.charm.message.ServerSortInventory;
import svenhjol.meson.MesonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySortingClient {
    private final MesonModule module;
    private final List<ImageButton> sortingButtons = new ArrayList<>();

    public final List<Class<? extends Screen>> tileScreens = new ArrayList<>();

    public InventorySortingClient(MesonModule module) {
        this.module = module;

        tileScreens.addAll(Arrays.asList(
            ChestScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            DispenserScreen.class,
            CrateScreen.class
        ));
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

        int x = screen.getGuiLeft() + 159;
        int y = screen.getGuiTop() - 19;

        List<Slot> slots = container.inventorySlots;
        for (Slot slot : slots) {
            if (tileScreens.contains(screen.getClass()) && slot.getSlotIndex() == 0) {
                this.addSortingButton(screen, x, y + slot.yPos, click -> {
                    module.mod.getPacketHandler().sendToServer(new ServerSortInventory(TILE));
                });
            }

            if (slot.inventory == mc.player.inventory) {
                if (screen instanceof InventoryScreen)
                    y += 76;

                this.addSortingButton(screen, x, y + slot.yPos, click -> {
                    module.mod.getPacketHandler().sendToServer(new ServerSortInventory(PLAYER));
                });
                break;
            }
        }

        this.sortingButtons.forEach(event::addWidget);
    }

    private void addSortingButton(ContainerScreen<?> screen, int x, int y, Button.IPressable onPress) {
        this.sortingButtons.add(new ImageButton(x, y, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, onPress));
    }
}
