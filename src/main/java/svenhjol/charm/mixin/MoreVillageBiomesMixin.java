package svenhjol.charm.mixin;

import net.minecraft.world.biome.BiomeMaker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BiomeMaker.class)
public class MoreVillageBiomesMixin {

//    /** Render plains village generation in Jungle BiomeMaker. */
//    @Inject(
//        method = "func_244213_a",
//        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/DefaultBiomeFeatures;func_243733_b(Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;)V"),
//        locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    private static void jungleGenerationHook(float f1, float f2, float f3, boolean b1, boolean b2, boolean b3, MobSpawnInfo.Builder mobs, CallbackInfoReturnable<Biome> cir, BiomeGenerationSettings.Builder builder) {
//        if (svenhjol.meson.Meson.enabled("charm:more_village_biomes"))
//            builder.func_242516_a(StructureFeatures.field_244154_t).func_242516_a(StructureFeatures.field_244135_a);
//    }
//
//    /** Render plains village generation in Swamp BiomeMaker. */
//    @Inject(
//        method = "func_244236_d",
//        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/DefaultBiomeFeatures;func_243742_f(Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;)V"),
//        locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    private static void swampGenerationHook(float f1, float f2, boolean b1, CallbackInfoReturnable<Biome> cir, MobSpawnInfo.Builder mobs, BiomeGenerationSettings.Builder builder) {
//        if (Meson.enabled("charm:more_village_biomes"))
//            builder.func_242516_a(StructureFeatures.field_244154_t).func_242516_a(StructureFeatures.field_244135_a);
//    }
}
