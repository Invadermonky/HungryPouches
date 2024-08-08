package com.invadermonky.hungrypouches.client.gui.screen;

import com.invadermonky.hungrypouches.client.gui.element.GuiButtonHP;
import com.invadermonky.hungrypouches.client.gui.element.GuiButtonPouchHP;
import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchSkeletal;
import com.invadermonky.hungrypouches.inventory.slots.SlotSkeletal;
import com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPouchSkeletal extends GuiCoreHP {
    protected ContainerPouchSkeletal container;

    public GuiPouchSkeletal(ContainerPouchSkeletal inventorySlotsIn) {
        super(inventorySlotsIn);
        this.container = inventorySlotsIn;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = -1;
        for(Slot slot : this.inventorySlots.inventorySlots) {
            if(slot instanceof SlotSkeletal && ((SlotSkeletal) slot).isPouchSlot()) {
                int xOffset = this.guiLeft + slot.xPos + 17;
                int yOffset = this.guiTop + slot.yPos - 1;
                //TODO: Change passed sprite to correct overlay.
                this.addButton(new GuiButtonPouchHP((SlotSkeletal) slot, ReferencesHP.SPRITE_BUTTON_ENABLE, i++, xOffset, yOffset));
                //TODO: Open Pouch button
                // this.addButton(new GuiButton(i++, xOffset, yOffset + 10, 8, 8, ""));
            }
        }
    }

    @Override
    public void drawSlot(Slot slotIn) {
        super.drawSlot(slotIn);
        //Need to handle the empty slot draw overlay here
        if(slotIn instanceof SlotSkeletal && !slotIn.getHasStack()) {
            SlotSkeletal slot = (SlotSkeletal) slotIn;
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
        if(button instanceof GuiButtonHP && ((GuiButtonHP) button).linkedSlot instanceof SlotSkeletal) {
            GuiButtonHP buttonHP = (GuiButtonHP) button;
            SlotSkeletal slot = (SlotSkeletal) buttonHP.linkedSlot;
            if(slot.isPouchSlot() && slot.getHasPouch() && buttonHP.enabled) {
                slot.setPouchEnabled(!slot.isPouchEnabled());
                short transactionId = this.mc.player.openContainer.getNextTransactionID(this.mc.player.inventory);
                sendMessageToServer(new MessageUpdateHeldItemHP(this.inventorySlots.windowId, transactionId, slot.slotIndex, slot.getStack()));
            }
        }
    }
}
