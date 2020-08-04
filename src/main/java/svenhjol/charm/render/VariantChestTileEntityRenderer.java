package svenhjol.charm.render;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.tileentity.VariantChestTileEntity;
import svenhjol.charm.tileentity.VariantTrappedChestTileEntity;
import svenhjol.meson.enums.IStorageMaterial;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NullableProblems")
@OnlyIn(Dist.CLIENT)
public class VariantChestTileEntityRenderer<T extends VariantChestTileEntity & IChestLid> extends ChestTileEntityRenderer<T> {
   private static final Map<IStorageMaterial, Map<ChestType, RenderMaterial>> normalTextures = new HashMap<>();
   private static final Map<IStorageMaterial, Map<ChestType, RenderMaterial>> trappedTextures = new HashMap<>();

   public VariantChestTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
      super(dispatcher);
   }

   public static void addTexture(IStorageMaterial material, ChestType chestType, ResourceLocation res, boolean trapped) {
      Map<IStorageMaterial, Map<ChestType, RenderMaterial>> textures = trapped
          ? VariantChestTileEntityRenderer.trappedTextures
          : VariantChestTileEntityRenderer.normalTextures;

      if (!textures.containsKey(material))
         textures.put(material, new HashMap<>());

      textures.get(material).put(chestType, new RenderMaterial(Atlases.CHEST_ATLAS, res));
   }

   @Override
   protected RenderMaterial getMaterial(T variant, ChestType chestType) {
      IStorageMaterial materialType = variant.getMaterialType();

      if (materialType != null) {
         Map<IStorageMaterial, Map<ChestType, RenderMaterial>> textures = variant instanceof VariantTrappedChestTileEntity
             ? trappedTextures
             : normalTextures;

         if (textures.containsKey(materialType))
            return textures.get(materialType).getOrDefault(chestType, Atlases.getChestMaterial(variant, chestType, false));
      }

      return Atlases.getChestMaterial(variant, chestType, false);
   }
}