package com.invadermonky.hungrypouches.client.gui.element;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.inventory.slots.SlotSkeletalPouch;

import javax.annotation.Nullable;

public class GuiButtonPouchHP extends GuiButtonHP {
    protected boolean inventoryButton;
    protected GuiSpriteHP buttonOverlay;

    public GuiButtonPouchHP(SlotSkeletalPouch slot, GuiSpriteHP buttonOverlay, int buttonId, int x, int y) {
        super(slot, buttonId, x, y);
        this.buttonOverlay = buttonOverlay;
    }

    @Override
    public boolean isButtonDown() {
        return ((SlotSkeletalPouch) this.linkedSlot).isPouchEnabled();
    }

    @Nullable
    @Override
    public GuiSpriteHP getButtonOverlay() {
        return null;
    }
}
