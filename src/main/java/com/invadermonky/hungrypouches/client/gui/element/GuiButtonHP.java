package com.invadermonky.hungrypouches.client.gui.element;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;

public abstract class GuiButtonHP extends GuiButton {
    public final Slot linkedSlot;
    public final GuiSpriteHP sprite;

    public GuiButtonHP(Slot slot, GuiSpriteHP sprite, int buttonId, int x, int y) {
        super(buttonId, x, y,sprite.width, sprite.height, "");
        this.linkedSlot = slot;
        this.sprite = sprite;
    }

    public abstract boolean isButtonDown();

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.checkEnabled();

            GuiSpriteHP.setRenderState(1.0f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            if(isButtonDown()) {
                this.sprite.spriteAlternate.drawSprite(this.x, this.y, this.getHoverState(this.hovered));
            } else {
                this.sprite.drawSprite(this.x, this.y, this.getHoverState(this.hovered));
            }

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        super.drawButtonForegroundLayer(mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        super.playPressSound(soundHandlerIn);
    }

    public void checkEnabled() {
        this.enabled = this.linkedSlot.getHasStack();
    }
}
