package com.invadermonky.hungrypouches.client.gui.util;

import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class GuiSpriteHP {
    private final int u;
    private final int v;
    public final int width;
    public final int height;
    public final GuiSpriteHP spriteAlternate;

    public GuiSpriteHP(int u, int v, int width, int height) {
        this(u,v,width,height,false);
    }

    public GuiSpriteHP(int u, int v, int width, int height, boolean hasAlternate) {
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.spriteAlternate = hasAlternate ? new GuiSpriteHP(u + this.width, v, width, height) : this;
    }

    public void drawSprite(int x, int y) {
        drawSprite(x, y, width, height);
    }

    public void drawSprite(int x, int y, double scale) {
        drawSprite(x, y, (int) (this.width * scale), (int) (this.height * scale));
    }

    public void drawSprite(int x, int y, int width, int height) {
        Gui.drawModalRectWithCustomSizedTexture(
                x,
                y,
                this.u,
                this.v,
                width,
                height,
                ReferencesHP.GUI_SPRITE_SHEET.getWidth(),
                ReferencesHP.GUI_SPRITE_SHEET.getHeight()
        );
    }

    public void drawSprite(int x, int y, int hoveredState) {
        drawSprite(x, y, width, height, hoveredState);
    }

    public void drawSprite(int x, int y, double scale, int hoveredState) {
        drawSprite(x, y, (int) (this.width * scale), (int) (this.height * scale), hoveredState);
    }

    public void drawSprite(int x, int y, int width, int height, int hoveredState) {
        Gui.drawModalRectWithCustomSizedTexture(
                x,
                y,
                this.u,
                this.v + height * hoveredState,
                width,
                height,
                ReferencesHP.GUI_SPRITE_SHEET.getWidth(),
                ReferencesHP.GUI_SPRITE_SHEET.getHeight()
        );
    }

    public static void setRenderState(float alpha) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getTextureManager().bindTexture(ReferencesHP.GUI_SPRITE_SHEET);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepth();
    }
}
