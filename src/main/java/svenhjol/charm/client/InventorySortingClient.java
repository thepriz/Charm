package svenhjol.charm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.HopperScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.client.event.GuiContainerEvent.DrawForeground;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.gui.BookcaseScreen;
import svenhjol.charm.gui.CrateScreen;
import svenhjol.charm.message.ServerSortInventory;
import svenhjol.meson.MesonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static svenhjol.charm.message.ServerSortInventory.PLAYER;
import static svenhjol.charm.message.ServerSortInventory.TILE;

public class InventorySortingClient {
    private static final int LEFT = 159;
    private static final int TOP = 12;
    private final MesonModule module;
    private final List<ImageButton> sortingButtons = new ArrayList<>();

    public final List<Class<? extends Screen>> tileScreens = new ArrayList<>();
    public final List<Class<? extends Screen>> blacklistScreens = new ArrayList<>();

    public InventorySortingClient(MesonModule module) {
        this.module = module;

        tileScreens.addAll(Arrays.asList(
            ChestScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            BookcaseScreen.class,
            DispenserScreen.class,
            CrateScreen.class
        ));

        blacklistScreens.addAll(Arrays.asList(
            CreativeScreen.class
        ));
    }

    @SubscribeEvent
    public void onInitGui(InitGuiEvent.Post event) {
        if (!module.enabled) return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (!(event.getGui() instanceof ContainerScreen))
            return;

        if (blacklistScreens.contains(event.getGui().getClass()))
            return;

        this.sortingButtons.clear();

        ContainerScreen<?> screen = (ContainerScreen<?>) event.getGui();
        Container container = screen.getContainer();

        int x = screen.getGuiLeft() + LEFT;
        int y = screen.getGuiTop() - TOP;

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

    @SubscribeEvent
    public void onDrawForeground(DrawForeground event) {
        if (!module.enabled) return;

        // redraw all buttons on inventory to handle recipe open/close
        if (event.getGuiContainer() instanceof InventoryScreen
            && !blacklistScreens.contains(event.getGuiContainer().getClass())
        ) {
            InventoryScreen screen = (InventoryScreen)event.getGuiContainer();
            this.sortingButtons.forEach(button -> button.setPosition(screen.getGuiLeft() + LEFT, button.y));
        }
    }

    private void addSortingButton(ContainerScreen<?> screen, int x, int y, Button.IPressable onPress) {
        this.sortingButtons.add(new ImageButton(x, y, 10, 10, 40, 0, 10, CharmResources.INVENTORY_BUTTONS, onPress));
    }
}
