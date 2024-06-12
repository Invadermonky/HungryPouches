package com.invadermonky.hungrypouches.client.gui.screen;

import com.invadermonky.hungrypouches.inventory.containers.ContainerVoidPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class GuiVoidPouch extends GuiCoreHP {
    protected EntityPlayer player;
    public GuiVoidPouch(ContainerVoidPouch inventorySlotsIn, EntityPlayer player) {
        super(inventorySlotsIn);
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        //TODO: Add buttons.
    }

    @Override
    public void drawSlot(Slot slotIn) {
        super.drawSlot(slotIn);
    }
}
