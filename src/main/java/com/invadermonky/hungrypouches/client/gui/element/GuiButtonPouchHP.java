package com.invadermonky.hungrypouches.client.gui.element;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.inventory.slots.SlotSkeletalPouch;

public class GuiButtonPouchHP extends GuiButtonHP {
    public GuiButtonPouchHP(SlotSkeletalPouch slot, GuiSpriteHP sprite, int buttonId, int x, int y) {
        super(slot, sprite, buttonId, x, y);
    }

    @Override
    public boolean isButtonDown() {
        return ((SlotSkeletalPouch) this.linkedSlot).isPouchEnabled();
    }
}
