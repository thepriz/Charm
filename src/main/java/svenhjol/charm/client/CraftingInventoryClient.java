package svenhjol.charm.client;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.message.ServerOpenCraftingTable;
import svenhjol.meson.MesonMod;

public class CraftingInventoryClient {
    private static final ResourceLocation CRAFTING_BUTTON_TEXTURE = new ResourceLocation(Charm.MOD_ID, "textures/gui/crafting_button.png");
    private final MesonMod mod;
    private ImageButton craftingButton;

    public CraftingInventoryClient(MesonMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onGuiContainer(GuiContainerEvent.DrawForeground event) {
        if (!(event.getGuiContainer() instanceof InventoryScreen))
            return;

        if (craftingButton == null)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if (mc.player.world.getGameTime() % 5 == 0)
            craftingButton.visible = mc.player.inventory.hasItemStack(new ItemStack(Blocks.CRAFTING_TABLE));

        InventoryScreen screen = (InventoryScreen)event.getGuiContainer();
        craftingButton.setPosition(screen.getGuiLeft() + 130, screen.height / 2 - 22);
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (!(event.getGui() instanceof InventoryScreen))
            return;

        InventoryScreen screen = (InventoryScreen)event.getGui();

        this.craftingButton = new ImageButton(screen.getGuiLeft() + 130, screen.height / 2 - 22, 20, 18, 0, 0, 19, CRAFTING_BUTTON_TEXTURE, click -> {
            this.mod.getPacketHandler().sendToServer(new ServerOpenCraftingTable());
        });

        if (!mc.player.inventory.hasItemStack(new ItemStack(Blocks.CRAFTING_TABLE)))
            craftingButton.visible = false;

        event.addWidget(this.craftingButton);
    }

    public void openInventory() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        mc.displayGuiScreen(new InventoryScreen(mc.player));
    }
}
