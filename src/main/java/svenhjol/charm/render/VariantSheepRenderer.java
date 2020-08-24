package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantSheepRenderer extends MobRenderer<SheepEntity, SheepModel<SheepEntity>> {
    public VariantSheepRenderer(EntityRendererManager manager) {
        super(manager, new SheepModel<>(), 0.7F);
        this.addLayer(new SheepWoolLayer(this));
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
