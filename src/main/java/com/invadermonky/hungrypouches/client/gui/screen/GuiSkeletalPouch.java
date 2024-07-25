package com.invadermonky.hungrypouches.client.gui.screen;

import com.invadermonky.hungrypouches.client.gui.element.GuiButtonHP;
import com.invadermonky.hungrypouches.client.gui.element.GuiButtonPouchHP;
import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.inventory.containers.ContainerSkeletalPouch;
import com.invadermonky.hungrypouches.inventory.slots.SlotSkeletalPouch;
import com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;
import java.io.IOException;

public class GuiSkeletalPouch extends GuiCoreHP {
    protected ContainerSkeletalPouch container;

    public GuiSkeletalPouch(ContainerSkeletalPouch inventorySlotsIn) {
        super(inventorySlotsIn);
        this.container = inventorySlotsIn;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = -1;
        for(Slot slot : this.inventorySlots.inventorySlots) {
            if(slot instanceof SlotSkeletalPouch && ((SlotSkeletalPouch) slot).isPouchSlot()) {
                int xOffset = this.guiLeft + slot.xPos + 17;
                int yOffset = this.guiTop + slot.yPos - 1;
                //TODO: Change passed sprite to correct overlay.
                this.addButton(new GuiButtonPouchHP((SlotSkeletalPouch) slot, ReferencesHP.SPRITE_BUTTON, i++, xOffset, yOffset));
                //TODO: Open Pouch button
                // this.addButton(new GuiButton(i++, xOffset, yOffset + 10, 8, 8, ""));
            }
        }
    }

    @Override
    public void drawSlot(Slot slotIn) {
        super.drawSlot(slotIn);
        //Need to handle the empty slot draw overlay here
        if(slotIn instanceof SlotSkeletalPouch && !slotIn.getHasStack()) {
            SlotSkeletalPouch slot = (SlotSkeletalPouch) slotIn;
            if (slot.isPouchSlot()) {
                GuiSpriteHP slotBackground = slot.validPouch.getSkeletalSlotBackgroundIcon();
                if (slotBackground != null) {
                    GuiSpriteHP.setRenderState(1.0f);
                    slotBackground.drawSprite(slot.xPos, slot.yPos);
                }
            }
        }
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) throws IOException {
        if(button instanceof GuiButtonHP && ((GuiButtonHP) button).linkedSlot instanceof SlotSkeletalPouch) {
            GuiButtonHP buttonHP = (GuiButtonHP) button;
            SlotSkeletalPouch slot = (SlotSkeletalPouch) buttonHP.linkedSlot;
            if(slot.isPouchSlot() && slot.getHasPouch() && buttonHP.enabled) {
                slot.setPouchEnabled(!slot.isPouchEnabled());
                short transactionId = this.mc.player.openContainer.getNextTransactionID(this.mc.player.inventory);
                sendMessageToServer(new MessageUpdateHeldItemHP(this.inventorySlots.windowId, transactionId, slot.slotIndex, slot.getStack()));
            }
        }
    }
}
