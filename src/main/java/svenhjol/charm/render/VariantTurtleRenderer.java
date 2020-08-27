package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TurtleRenderer;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantTurtleRenderer extends TurtleRenderer {
    public VariantTurtleRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() { return VariantTurtleRenderer::new; }

    @Override
    public ResourceLocation getEntityTexture(TurtleEntity entity) {
        return VariantAnimalTextures.getTurtleTexture(entity);
    }
}
