package svenhjol.charm.decoration.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.decoration.client.renderer.LockerTileEntityRenderer;
import svenhjol.charm.decoration.module.Lockers;

public class LockersClient {
    public static final ResourceLocation DOUBLE_RESOURCE = new ResourceLocation(Charm.MOD_ID, "entity/locker/normal_double");
    public static final ResourceLocation SINGLE_RESOURCE = new ResourceLocation(Charm.MOD_ID, "entity/locker/normal");

    public LockersClient() {
        ClientRegistry.bindTileEntityRenderer(Lockers.tile, LockerTileEntityRenderer::new );
//        ClientRegistry.bindTileEntitySpecialRenderer(Lockers.tile, );
    }
}