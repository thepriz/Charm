package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantWolfRenderer extends WolfRenderer {
    public VariantWolfRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantWolfRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(WolfEntity entity) {
        return VariantAnimalTextures.getWolfTexture(entity);
    }
}
