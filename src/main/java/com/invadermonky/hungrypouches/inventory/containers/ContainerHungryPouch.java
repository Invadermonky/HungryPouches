package com.invadermonky.hungrypouches.inventory.containers;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.slots.SlotHungryPouch;
import com.invadermonky.hungrypouches.inventory.slots.SlotVoid;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchVoid;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHungryPouch extends ContainerCoreHP {
    protected final int rowSize;
    protected final boolean isVoid;

    public ContainerHungryPouch(ItemStack pouch, InventoryPlayer inventory) {
        super(pouch, inventory);
        this.rowSize = PouchHandler.getMaxSlots(this.pouch) / 3;
        this.isVoid = this.pouch.getItem() instanceof ItemPouchVoid;
        this.bindPouchInventory();
    }

    @Override
    protected void bindPouchInventory() {
        int xOffset;
        int yOffset;

        if(isVoid) {
            xOffset = 80;
            yOffset = 36;
            this.addSlotToContainer(new SlotVoid(this.containerWrapper, 0, xOffset, yOffset));
        } else {
            xOffset = (9 + 9 * 9) - (rowSize * 10);
            yOffset = 16;
            for (int i = 0; i < PouchHandler.getMaxSlots(this.pouch); i++) {
                this.addSlotToContainer(new SlotHungryPouch(this.containerWrapper, i, xOffset + i % this.rowSize * 20, yOffset + i / this.rowSize * 20));
            }
        }
    }


}
