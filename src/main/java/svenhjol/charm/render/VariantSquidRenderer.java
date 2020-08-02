package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantSquidRenderer extends SquidRenderer {
    public VariantSquidRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantSquidRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(SquidEntity entity) {
        return VariantAnimalTextures.getSquidTexture(entity);
    }
}
