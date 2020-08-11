package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantEnderDragonRenderer extends EnderDragonRenderer {
    public VariantEnderDragonRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return VariantEnderDragonRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(EnderDragonEntity entity) {
        return VariantAnimalTextures.getEnderDragonTexture(entity);
    }
}
