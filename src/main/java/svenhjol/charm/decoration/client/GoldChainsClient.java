package svenhjol.charm.decoration.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import svenhjol.charm.decoration.module.GoldChains;

public class GoldChainsClient {
    public GoldChainsClient() {
        RenderType transparentRenderType = RenderType.getCutoutMipped();
        RenderTypeLookup.setRenderLayer(GoldChains.block, transparentRenderType);
    }
}
