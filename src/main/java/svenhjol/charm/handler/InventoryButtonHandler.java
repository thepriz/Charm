package svenhjol.charm.handler;

import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.module.InventoryCrafting;
import svenhjol.charm.module.InventoryEnderChest;

public class InventoryButtonHandler {
    public ImageButton recipeButton;

    @SubscribeEvent
    public void onInitGui(InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof InventoryScreen) || event.getGui() instanceof CreativeScreen)
            return;

        // get recipe button from the widgetlist
        if (!event.getWidgetList().isEmpty() && event.getWidgetList().get(0) instanceof ImageButton)
            this.recipeButton = (ImageButton)event.getWidgetList().get(0);
    }

    @SubscribeEvent
    public void onDrawForeground(GuiContainerEvent.DrawForeground event) {
        if (!(event.getGuiContainer() instanceof InventoryScreen) || event.getGuiContainer() instanceof CreativeScreen)
            return;

        InventoryScreen screen = (InventoryScreen)event.getGuiContainer();

        int y = screen.height / 2 - 22;
        int left = screen.getGuiLeft();

        if (InventoryCrafting.client.isButtonVisible()) {
            if (InventoryEnderChest.client.isButtonVisible()) {

                // recipe, crafting and chest buttons
                if (this.recipeButton != null)
                    this.recipeButton.visible = false;
                InventoryCrafting.client.craftingButton.setPosition(left + 104, y);
                InventoryEnderChest.client.chestButton.setPosition(left + 130, y);
            } else {

                // just the recipe and crafting buttons
                if (this.recipeButton != null)
                    this.recipeButton.visible = true;
                InventoryCrafting.client.craftingButton.setPosition(left + 130, y);
            }
        } else if (InventoryEnderChest.client.isButtonVisible()) {

            // just the recipe and chest buttons
            if (this.recipeButton != null)
                this.recipeButton.visible = true;
            InventoryEnderChest.client.chestButton.setPosition(left + 130, y);
        }
    }
}
