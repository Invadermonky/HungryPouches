package com.invadermonky.hungrypouches.client.gui.screen;

import com.invadermonky.hungrypouches.client.gui.element.GuiButtonFilterHP;
import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchVoid;
import com.invadermonky.hungrypouches.inventory.slots.SlotFilter;
import com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPouchVoid extends GuiCoreHP {
    protected EntityPlayer player;
    public GuiPouchVoid(ContainerPouchVoid inventorySlotsIn, EntityPlayer player) {
        super(inventorySlotsIn);
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = -1;
        for(Slot slot : this.inventorySlots.inventorySlots) {
            if(slot instanceof SlotFilter) {
                int xOffset = this.guiLeft + slot.xPos - 1;
                int yOffset = this.guiTop + slot.yPos + 20;
                this.addButton(new GuiButtonFilterHP((SlotFilter) slot, ReferencesHP.SPRITE_BUTTON_META, true, i++, xOffset, yOffset));
                this.addButton(new GuiButtonFilterHP((SlotFilter) slot, ReferencesHP.SPRITE_BUTTON_ORE, false, i++, xOffset + 10, yOffset));
            }
        }
    }

    @Override
    public void drawSlot(Slot slotIn) {
        super.drawSlot(slotIn);
        if(slotIn instanceof SlotFilter && !slotIn.getHasStack()) {
            GuiSpriteHP.setRenderState(1.0f);
            ReferencesHP.SPRITE_SLOT_FILTER.drawSprite(slotIn.xPos, slotIn.yPos);
        }
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        this.buttonList.forEach(button -> {
            if(button.isMouseOver() && button.enabled && button instanceof GuiButtonFilterHP) {
                GuiButtonFilterHP buttonFilter = (GuiButtonFilterHP) button;
                this.drawHoveringText(I18n.format(buttonFilter.getTooltip()), x + 1, y + 20);
            }
        });
        super.renderHoveredToolTip(x, y);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof GuiButtonFilterHP && ((GuiButtonFilterHP) button).linkedSlot instanceof SlotFilter) {
            GuiButtonFilterHP buttonFilterHP = (GuiButtonFilterHP) button;
            SlotFilter slot = (SlotFilter) buttonFilterHP.linkedSlot;
            if(buttonFilterHP.enabled) {
                if (buttonFilterHP.metaButton) {
                    slot.setMatchMeta(!buttonFilterHP.isButtonDown());
                } else {
                    slot.setMatchOre(!buttonFilterHP.isButtonDown());
                }
                short transactionId = this.mc.player.openContainer.getNextTransactionID(this.mc.player.inventory);
                sendMessageToServer(new MessageUpdateHeldItemHP(this.inventorySlots.windowId, transactionId, slot.slotIndex, slot.getMatchMeta(), slot.getMatchOre()));
            }
        }
    }
}
