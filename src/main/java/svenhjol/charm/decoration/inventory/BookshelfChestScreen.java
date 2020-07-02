package svenhjol.charm.decoration.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import svenhjol.charm.Charm;
import svenhjol.charm.decoration.container.BookshelfChestContainer;

public class BookshelfChestScreen extends ContainerScreen<BookshelfChestContainer> implements IHasContainer<BookshelfChestContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Charm.MOD_ID, "textures/gui/generic_9.png");

    public BookshelfChestScreen(BookshelfChestContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.field_230711_n_ = true;
        this.xSize = 175;
        this.ySize = 131;
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(matrix);
        super.func_230430_a_(matrix, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrix, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack matrix, int mouseX, int mouseY) {
        this.field_230712_o_.func_238422_b_(matrix, this.field_230704_d_, 8.0F, 6.0F, 4210752);
        this.field_230712_o_.func_238422_b_(matrix, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.field_230706_i_ != null) {
            this.field_230706_i_.getTextureManager().bindTexture(GUI_TEXTURE);
            int x = (this.field_230708_k_ - this.xSize) / 2;
            int y = (this.field_230709_l_ - this.ySize) / 2;
            this.func_238474_b_(matrix, x, y, 0, 0, this.xSize, this.ySize);
        }
    }
}
