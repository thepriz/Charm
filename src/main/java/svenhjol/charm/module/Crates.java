package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.block.CrateBlock;
import svenhjol.charm.client.CratesClient;
import svenhjol.charm.container.CrateContainer;
import svenhjol.charm.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class Crates extends MesonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "crate");
    public static final Map<IStorageMaterial, CrateBlock> CRATE_BLOCKS = new HashMap<>();

    public static ContainerType<CrateContainer> CONTAINER;
    public static TileEntityType<CrateTileEntity> TILE;

    // add blocks and items to these lists to blacklist them from crates
    public static final List<Class<? extends Block>> invalidBlocks = new ArrayList<>();
    public static final List<Class<? extends Item>> invalidItems = new ArrayList<>();

    public static CratesClient client;

    @Config(name = "Show tooltip", description = "If true, hovering over a crate will show its contents in a tooltip.")
    public static boolean showTooltip = true;

    @Module(description = "A smaller storage solution with the benefit of being transportable.")
    public Crates() {}

    @Override
    public void init() {
        for (VanillaStorageMaterial type : VanillaStorageMaterial.values()) {
            CRATE_BLOCKS.put(type, new CrateBlock(this, type));
        }

        invalidBlocks.add(ShulkerBoxBlock.class);
        invalidBlocks.add(CrateBlock.class);

        CONTAINER = new ContainerType<>(CrateContainer::instance);
        TILE = TileEntityType.Builder.create(CrateTileEntity::new).build(null);

        mod.register(CONTAINER, ID);
        mod.register(TILE, ID);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        client = new CratesClient();
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static boolean canContainItem(ItemStack stack) {
        return !invalidItems.contains(stack.getItem().getClass()) && !invalidBlocks.contains(ItemHelper.getBlockClass(stack));
    }

    public static CrateBlock getRandomCrateBlock(Random rand) {
        List<CrateBlock> values = new ArrayList<>(Crates.CRATE_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
