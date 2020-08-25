package svenhjol.charm.client;

import net.minecraft.block.Blocks;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiContainerEvent.DrawForeground;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.message.ServerOpenEnderChest;
import svenhjol.charm.module.InventoryEnderChest;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;

public class InventoryEnderChestClient {
    private final MesonModule module;
    public ImageButton chestButton;

    public InventoryEnderChestClient(MesonModule module) {
        this.module = module;
    }

    @SubscribeEvent
    public void onInitGui(InitGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (!(event.getGui() instanceof InventoryScreen))
            return;

        InventoryScreen screen = (InventoryScreen) event.getGui();

        this.chestButton = new ImageButton(screen.getGuiLeft() + 130, screen.height / 2 - 22, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
            this.module.mod.getPacketHandler().sendToServer(new ServerOpenEnderChest());
        });

        chestButton.visible = this.hasChest(mc.player);
        event.addWidget(this.chestButton);
    }

    @SubscribeEvent
    public void onDrawForeground(DrawForeground event) {
        if (!(event.getGuiContainer() instanceof InventoryScreen))
            return;

        if (chestButton == null)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if (mc.player.world.getGameTime() % 5 == 0)
            chestButton.visible = this.hasChest(mc.player);
    }

    public boolean hasChest(PlayerEntity player) {
        return InventoryEnderChest.offhandOnly && ItemHelper.getBlockClass(player.getHeldItemOffhand()) == EnderChestBlock.class
            || !InventoryEnderChest.offhandOnly && player.inventory.hasItemStack(new ItemStack(Blocks.ENDER_CHEST));
    }

    public boolean isButtonVisible() {
        return this.chestButton != null && this.chestButton.visible;
    }
}