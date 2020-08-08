package svenhjol.charm.module;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.block.BookcaseBlock;
import svenhjol.charm.block.QuarkBookcaseBlock;
import svenhjol.charm.container.BookcaseContainer;
import svenhjol.charm.gui.BookcaseScreen;
import svenhjol.charm.tileentity.BookcaseTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.*;

public class Bookcases extends MesonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "bookcase");
    public static final Map<IStorageMaterial, BookcaseBlock> BOOKCASE_BLOCKS = new HashMap<>();
    public static final Map<IStorageMaterial, BookcaseBlock> QUARK_BOOKCASE_BLOCKS = new HashMap<>();

    public static ContainerType<BookcaseContainer> CONTAINER;
    public static TileEntityType<BookcaseTileEntity> TILE;

    public static List<Class<? extends Item>> validItems = new ArrayList<>();

    @Config(name = "Valid books", description = "Additional items that may be placed in bookcases.")
    public static List<String> configValidItems = Arrays.asList(
        "quark:ancient_tome",
        "strange:scroll"
    );

    @Module(description = "Bookshelves that can hold up to 9 stacks of books and maps.")
    public Bookcases() {
        validItems.addAll(Arrays.asList(
            Items.BOOK.getClass(),
            Items.ENCHANTED_BOOK.getClass(),
            Items.WRITTEN_BOOK.getClass(),
            Items.WRITABLE_BOOK.getClass(),
            Items.KNOWLEDGE_BOOK.getClass(),
            Items.PAPER.getClass(),
            Items.MAP.getClass(),
            Items.FILLED_MAP.getClass()
        ));
    }

    @Override
    public void init() {
        VanillaStorageMaterial.getTypes().forEach(type -> {
            BOOKCASE_BLOCKS.put(type, new BookcaseBlock(this, type));
        });

        VanillaStorageMaterial.getTypesWithout(VanillaStorageMaterial.OAK).forEach(type -> {
            QUARK_BOOKCASE_BLOCKS.put(type, new QuarkBookcaseBlock(this, type));
        });

        CONTAINER = new ContainerType<>(BookcaseContainer::instance);
        TILE = TileEntityType.Builder.create(BookcaseTileEntity::new).build(null);

        mod.register(CONTAINER, ID);
        mod.register(TILE, ID);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        configValidItems.forEach(string -> {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if (item != null)
                validItems.add(item.getClass());
        });
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CONTAINER, BookcaseScreen::new);
    }

    public static boolean canInsertItem(ItemStack stack) {
        return validItems.contains(stack.getItem().getClass());
    }
}
