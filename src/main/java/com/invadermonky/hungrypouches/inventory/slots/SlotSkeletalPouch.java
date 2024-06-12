package com.invadermonky.hungrypouches.inventory.slots;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.wrappers.InventoryContainerWrapperHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotSkeletalPouch extends Slot {
    public final InventoryContainerWrapperHP containerWrapper;
    public final int slotIndex;
    public final IHungryPouch validPouch;

    /**
     * Creates a slot locked to a specific Hungry Pouch item.
     */
    public SlotSkeletalPouch(InventoryContainerWrapperHP inventoryIn, int index, int xPosition, int yPosition, @Nullable Item validPouch) {
        super(inventoryIn, index, xPosition, yPosition);
        this.containerWrapper = inventoryIn;
        this.slotIndex = index;
        this.validPouch = (validPouch instanceof IHungryPouch) ? (IHungryPouch) validPouch : null;
    }

    /**
     * Creates a slot that will accept all items excluding Hungry Pouches.
     */
    public SlotSkeletalPouch(InventoryContainerWrapperHP inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, null);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.isPouchSlot() ? stack.getItem() == validPouch : !(stack.getItem() instanceof AbstractPouchHP);
    }

    public boolean isPouchSlot() {
        return validPouch != null;
    }

    public boolean getHasPouch() {
        return !this.getStack().isEmpty() && this.getStack().getItem() instanceof AbstractPouchHP;
    }

    public boolean isPouchEnabled() {
        return getHasPouch() && PouchHandler.isEnabled(this.getStack());
    }

    public void setPouchEnabled(boolean enabled) {
        if(this.getHasPouch()) {
            PouchHandler.setEnabled(this.getStack(), enabled);
            this.containerWrapper.setInventorySlotContents(this.slotIndex, this.getStack());
        }
    }
}
