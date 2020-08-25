package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.module.PortableEnderChest;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.ScreenHelper;

public class PortableEnderChestClient {
    public TexturedButtonWidget chestButton;

    public PortableEnderChestClient(MesonModule module) {
        // set up client listeners
        SetupGuiCallback.EVENT.register((client, width, height, buttons, addButton) -> {
            if (client.player == null)
                return;

            if (!(client.currentScreen instanceof InventoryScreen))
                return;

            InventoryScreen screen = (InventoryScreen)client.currentScreen;
            int guiLeft = ScreenHelper.getX(screen);

            this.chestButton = new TexturedButtonWidget(guiLeft + 130, height / 2 - 22, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
                ClientSidePacketRegistry.INSTANCE.sendToServer(PortableEnderChest.MSG_SERVER_OPEN_ENDER_CHEST, new PacketByteBuf(Unpooled.buffer()));
            });

            this.chestButton.visible = hasChest(client.player);
            addButton.accept(this.chestButton);
        });

        RenderGuiCallback.EVENT.register((client, matrices, mouseX, mouseY, delta) -> {
            if (!(client.currentScreen instanceof InventoryScreen)
                || this.chestButton == null
                || client.player == null
            ) {
                return;
            }

            if (client.player.world.getTime() % 5 == 0)
                this.chestButton.visible = hasChest(client.player);
        });
    }

    private boolean hasChest(PlayerEntity player) {
        return PortableEnderChest.offhandOnly && ItemHelper.getBlockClass(player.getOffHandStack()) == EnderChestBlock.class
            || !PortableEnderChest.offhandOnly && player.inventory.contains(new ItemStack(Blocks.ENDER_CHEST));
    }

    public boolean isButtonVisible() {
        return this.chestButton != null && this.chestButton.visible;
    }
}