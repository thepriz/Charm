package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantPigRenderer extends PigRenderer {
    public VariantPigRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantPigRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(PigEntity entity) {
        return VariantAnimalTextures.getPigTexture(entity);
    }
}
