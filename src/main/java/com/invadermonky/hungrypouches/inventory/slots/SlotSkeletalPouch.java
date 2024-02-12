package com.invadermonky.hungrypouches.inventory.slots;

import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotSkeletalPouch extends Slot {
    protected final AbstractPouchHP validPouch;
    protected final boolean isPouchSlot;

    /**
     * Creates a slot locked to a specific Hungry Pouch item.
     *
     * @param inventoryIn Inventory this slot is part of.
     * @param index Slot index value.
     * @param xPosition Slot x-position.
     * @param yPosition Slot y-position.
     * @param validPouch Valid pouch item that can be stored in this slot.
     */
    public SlotSkeletalPouch(IInventory inventoryIn, int index, int xPosition, int yPosition, @Nullable Item validPouch) {
        super(inventoryIn, index, xPosition, yPosition);
        this.validPouch = (validPouch instanceof AbstractPouchHP) ? (AbstractPouchHP) validPouch : null;
        this.isPouchSlot = validPouch != null;
    }

    /**
     * Creates a slot that will accept all items excluding Hungry Pouches.
     *
     * @param inventoryIn Inventory this slot is part of.
     * @param index Slot index value.
     * @param xPosition Slot x-position.
     * @param yPosition Slot y-position.
     */
    public SlotSkeletalPouch(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, null);
    }

    public boolean getIsPouchSlot() {
        return this.isPouchSlot;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return isPouchSlot ? stack.getItem() == validPouch : !(stack.getItem() instanceof AbstractPouchHP);
    }
}
