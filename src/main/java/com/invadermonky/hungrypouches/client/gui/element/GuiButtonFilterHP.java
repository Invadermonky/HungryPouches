package com.invadermonky.hungrypouches.client.gui.element;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.inventory.slots.SlotFilter;

public class GuiButtonFilterHP extends GuiButtonHP {
    protected final GuiSpriteHP buttonOverlay;
    /** True if this button is a Meta-matching filter button, false if it is an Ore-matching filter button. */
    protected final boolean metaButton;

    public GuiButtonFilterHP(SlotFilter slot, GuiSpriteHP buttonOverlay, boolean metaButton, int buttonId, int x, int y) {
        super(slot, buttonId, x, y);
        this.buttonOverlay = buttonOverlay;
        this.metaButton = metaButton;
    }

    protected SlotFilter getSlot() {
        return (SlotFilter) this.linkedSlot;
    }

    @Override
    public boolean isButtonDown() {
        return this.metaButton ? getSlot().getMatchMeta() : getSlot().getMatchOre();
    }

    @Override
    public GuiSpriteHP getButtonOverlay() {
        return null;
    }
}
