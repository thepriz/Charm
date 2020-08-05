package svenhjol.charm.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import svenhjol.charm.Charm;
import svenhjol.charm.message.ClientOpenInventory;
import svenhjol.meson.Meson;

public class InventoryEnderChestContainer extends ChestContainer {
    public InventoryEnderChestContainer(int i, PlayerInventory playerInv, EnderChestInventory chestInv) {
        super(ContainerType.GENERIC_9X3, i, playerInv, chestInv, 3);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);

        if (!playerIn.world.isRemote)
            Meson.getMod(Charm.MOD_ID).getPacketHandler().sendToPlayer(new ClientOpenInventory(), (ServerPlayerEntity)playerIn);
    }
}
