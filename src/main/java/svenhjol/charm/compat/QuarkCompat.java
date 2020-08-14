//package svenhjol.charm.compat;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.EnchantmentData;
//import net.minecraft.item.DyeColor;
//import net.minecraft.item.EnchantedBookItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.feature.structure.Structure;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraftforge.registries.ForgeRegistries;
//import svenhjol.charm.iface.IQuarkCompat;
//import svenhjol.meson.enums.VanillaStorageMaterial;
//import svenhjol.meson.helper.WorldHelper;
//import vazkii.quark.base.Quark;
//import vazkii.quark.base.module.ModuleLoader;
//import vazkii.quark.building.module.QuiltedWoolModule;
//import vazkii.quark.building.module.VariantChestsModule;
//import vazkii.quark.tools.module.AncientTomesModule;
//import vazkii.quark.world.block.CaveCrystalBlock;
//import vazkii.quark.world.module.BigDungeonModule;
//
//import javax.annotation.Nullable;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//public class QuarkCompat implements IQuarkCompat {
//
//    @Override
//    public boolean isInsideBigDungeon(ServerWorld world, BlockPos pos) {
//        return WorldHelper.isInsideStructure(world, pos, getBigDungeonStructure());
//    }
//
//    @Override
//    public boolean isCaveCrystalBlock(World world, BlockPos pos) {
//        BlockState state = world.getBlockState(pos);
//        return state.getBlock() instanceof CaveCrystalBlock;
//    }
//
//    @Override
//    public boolean hasVariantChests() {
//        return ModuleLoader.INSTANCE.isModuleEnabled(VariantChestsModule.class);
//    }
//
//    @Override
//    public boolean hasQuiltedWool() {
//        return ModuleLoader.INSTANCE.isModuleEnabled(QuiltedWoolModule.class);
//    }
//
//    @Override
//    public boolean hasBigDungeons() {
//        return ModuleLoader.INSTANCE.isModuleEnabled(BigDungeonModule.class);
//    }
//
//    @Override
//    public Structure<?> getBigDungeonStructure() {
//        return BigDungeonModule.STRUCTURE;
//    }
//
//    @Nullable
//    @Override
//    public Block getRandomChest(Random rand) {
//        List<VanillaStorageMaterial> types = Arrays.asList(VanillaStorageMaterial.values());
//        VanillaStorageMaterial type = types.get(rand.nextInt(types.size()));
//
//        ResourceLocation res = new ResourceLocation(Quark.MOD_ID, type.name().toLowerCase() + "_chest");
//        return ForgeRegistries.BLOCKS.getValue(res);
//    }
//
//    @Override
//    public ItemStack getRandomAncientTome(Random rand) {
//        List<Enchantment> validEnchants = AncientTomesModule.validEnchants;
//        ItemStack tome = new ItemStack(AncientTomesModule.ancient_tome);
//
//        Enchantment enchantment = validEnchants.get(rand.nextInt(validEnchants.size()));
//        EnchantedBookItem.addEnchantment(tome, new EnchantmentData(enchantment, enchantment.getMaxLevel()));
//
//        return tome;
//    }
//
//    @Override
//    public ItemStack getQuiltedWool(DyeColor color) {
//        final Item wool = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Quark.MOD_ID, color.getString().toLowerCase() + "_quilted_wool"));
//        if (wool != null)
//            return new ItemStack(wool);
//
//        return ItemStack.EMPTY;
//    }
//}
