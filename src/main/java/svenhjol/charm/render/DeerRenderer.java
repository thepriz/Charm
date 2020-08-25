package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import svenhjol.charm.entity.DeerEntity;
import svenhjol.charm.models.DeerModel;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeerRenderer extends MobRenderer<DeerEntity, DeerModel<DeerEntity>> {
    private static final ResourceLocation DEER_TEXTURES = new ResourceLocation("textures/entity/deer/deer.png");

    public DeerRenderer(EntityRendererManager manager) {
        super(manager, new DeerModel<>(), 0.7F); // the 0.7F is the shadow size of the entity
    }

    @Override
    public ResourceLocation getEntityTexture(DeerEntity entity) {
        return DEER_TEXTURES;
    }
}