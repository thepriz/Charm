package svenhjol.charm.decoration.module;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.LockerBlock;
import svenhjol.charm.decoration.client.LockersClient;
import svenhjol.charm.decoration.container.LockerContainer;
import svenhjol.charm.decoration.inventory.LockerScreen;
import svenhjol.charm.decoration.tileentity.LockerTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION)
public class Lockers extends MesonModule {
    public static LockersClient client;
    public static LockerBlock locker;

    @ObjectHolder("charm:locker")
    public static TileEntityType<LockerTileEntity> tile;

    @ObjectHolder("charm:locker")
    public static ContainerType<LockerContainer> container;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void init() {
        locker = new LockerBlock(this);
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "locker");
        tile = TileEntityType.Builder.create(LockerTileEntity::new, locker).build(null);
        RegistryHandler.registerTile(tile, res);
    }

    @Override
    public void initClient() {
        ScreenManager.registerFactory(container, LockerScreen::new);
        client = new LockersClient();
        MinecraftForge.EVENT_BUS.register(client);
    }
}
