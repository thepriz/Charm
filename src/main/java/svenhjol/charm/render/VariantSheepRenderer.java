package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantSheepRenderer extends SheepRenderer {
    public VariantSheepRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantSheepRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(SheepEntity entity) {
        return VariantAnimalTextures.getSheepTexture(entity);
    }
}
