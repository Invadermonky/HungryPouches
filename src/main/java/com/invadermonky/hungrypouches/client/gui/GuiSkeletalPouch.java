package com.invadermonky.hungrypouches.client.gui;

import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GuiSkeletalPouch extends GuiCoreHP {

    public GuiSkeletalPouch(Container inventorySlotsIn, ItemStack pouch) {
        super(inventorySlotsIn, pouch);
        this.xSize = 176;
        this.ySize = 185;
    }

    @Override
    public void initGui() {
        super.initGui();
        //TODO: Menu Buttons
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        this.mc.renderEngine.bindTexture(ReferencesHP.GUI_POUCH_SKELETAL);
        if(this.xSize <= 256 && this.ySize <= 256) {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        } else {
            this.drawSizedTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, 512.0F, 512.0F);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) guiLeft, (float) this.guiTop, 0);
        GlStateManager.popMatrix();
    }
}
