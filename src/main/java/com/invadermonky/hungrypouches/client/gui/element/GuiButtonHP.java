package com.invadermonky.hungrypouches.client.gui.element;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;

import static com.invadermonky.hungrypouches.util.ReferencesHP.SPRITE_BUTTON;

public abstract class GuiButtonHP extends GuiButton {
    public final Slot linkedSlot;

    public GuiButtonHP(Slot slot, int buttonId, int x, int y) {
        super(buttonId, x, y, SPRITE_BUTTON.width, SPRITE_BUTTON.height, "");
        this.linkedSlot = slot;
    }

    public abstract boolean isButtonDown();

    public abstract GuiSpriteHP getButtonOverlay();

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.checkEnabled();

            GuiSpriteHP.setRenderState(1.0f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            if(isButtonDown()) {
                SPRITE_BUTTON.spriteAlternate.drawSprite(this.x, this.y, this.getHoverState(this.hovered));
            } else {
                SPRITE_BUTTON.drawSprite(this.x, this.y, this.getHoverState(this.hovered));
            }

            if(getButtonOverlay() != null && this.enabled)
                getButtonOverlay().drawSprite(this.x + 1, this.y + 1);

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    private void checkEnabled() {
        this.enabled = this.linkedSlot.getHasStack();
    }
}
