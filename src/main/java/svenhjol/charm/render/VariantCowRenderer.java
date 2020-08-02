package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantCowRenderer extends CowRenderer {
    public VariantCowRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantCowRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(CowEntity entity) {
        return VariantAnimalTextures.getCowTexture(entity);
    }
}
