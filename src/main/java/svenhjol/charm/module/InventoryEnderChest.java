package svenhjol.charm.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import svenhjol.charm.client.InventoryEnderChestClient;
import svenhjol.charm.container.InventoryEnderChestContainer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

public class InventoryEnderChest extends MesonModule {
    private static final ITextComponent LABEL = new TranslationTextComponent("container.charm.portable_ender_chest");
    public static InventoryEnderChestClient client;

    @Config(name = "Offhand only", description = "If true, the chest button will only appear when the player holds the ender chest in their offhand.")
    public static boolean offhandOnly = false;

    @Module(description = "Allows access to chest contents if the player has an Ender Chest in their inventory.")
    public InventoryEnderChest() {}

    @Override
    public void initClient() {
        client = new InventoryEnderChestClient(this);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 0.4F, 1.08F);
        player.openContainer(new SimpleNamedContainerProvider((i, inv, p) -> new InventoryEnderChestContainer(i, inv, p.getInventoryEnderChest()), LABEL));
    }
}
