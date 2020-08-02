package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantChickenRenderer extends ChickenRenderer {
    public VariantChickenRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantChickenRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(ChickenEntity entity) {
        return VariantAnimalTextures.getChickenTexture(entity);
    }
}
