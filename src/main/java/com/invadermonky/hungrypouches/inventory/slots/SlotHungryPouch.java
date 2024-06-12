package com.invadermonky.hungrypouches.inventory.slots;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.wrappers.InventoryContainerWrapperHP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotHungryPouch extends Slot {
    protected final InventoryContainerWrapperHP inventoryWrapper;
    protected final ItemStack pouch;

    public SlotHungryPouch(InventoryContainerWrapperHP inventoryWrapper, int index, int xPosition, int yPosition) {
        super(inventoryWrapper, index, xPosition, yPosition);
        this.inventoryWrapper = inventoryWrapper;
        this.pouch = inventoryWrapper.getContainerStack();
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return PouchHandler.isWhitelistedItem(this.pouch, stack);
    }

    @Override
    public int getSlotStackLimit() {
        return PouchHandler.getMaxStackSize(this.pouch);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return Math.max(PouchHandler.getMaxStackSize(this.pouch, stack), this.getSlotStackLimit());
    }
}
