package com.invadermonky.hungrypouches.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class GuiCoreHP extends GuiContainer {
    protected ItemStack pouch;

    public GuiCoreHP(Container inventorySlotsIn, ItemStack pouch) {
        super(inventorySlotsIn);
        this.pouch = pouch;
    }

    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float texW, float texH) {
        float texU = 1.0F / texW;
        float texV = 1.0F / texH;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        buffer.pos((x),         (y + height), this.zLevel).tex(((u)         * texU), ((v + height) * texV)).endVertex();
        buffer.pos((x + width), (y + height), this.zLevel).tex(((u + width) * texU), ((v + height) * texV)).endVertex();
        buffer.pos((x + width), (y),          this.zLevel).tex(((u + width) * texU), ((v)          * texV)).endVertex();
        buffer.pos((x),         (y),          this.zLevel).tex(((u)         * texU), ((v)          * texV)).endVertex();

        Tessellator.getInstance().draw();
    }
}
