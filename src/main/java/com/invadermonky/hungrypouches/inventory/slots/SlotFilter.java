package com.invadermonky.hungrypouches.inventory.slots;

import com.invadermonky.hungrypouches.inventory.wrappers.FilterContainerWrapperHP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotFilter extends Slot {
    protected final FilterContainerWrapperHP filterWrapper;
    public final int slotIndex;
    public boolean matchMeta;
    public boolean matchOre;

    public SlotFilter(FilterContainerWrapperHP inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.filterWrapper = inventoryIn;
        this.slotIndex = index;
        this.matchMeta = inventoryIn.getSlotMatchMeta(index);
        this.matchOre = inventoryIn.getSlotMatchOre(index);
    }

    public boolean getMatchMeta() {
        return this.matchMeta;
    }

    public void setMatchMeta(boolean matchMeta) {
        this.matchMeta = matchMeta;
        this.filterWrapper.setSlotMatchMeta(this.slotIndex, this.matchMeta);
    }

    public String getItemString() {
        return this.filterWrapper.getItemString(this.slotIndex);
    }

    public boolean getMatchOre() {
        return this.matchOre;
    }

    public void setMatchOre(boolean matchOre) {
        this.matchOre = matchOre;
        this.filterWrapper.setSlotMatchOre(this.slotIndex, this.matchOre);
    }

    public int[] getOreDicts() {
        return this.filterWrapper.getSlotOreDicts(this.slotIndex);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        if(this.isItemValid(stack)) {
            if(!stack.isEmpty()) {
                stack.setCount(1);
            }
        }
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean canTakeStack(@Nonnull EntityPlayer playerIn) {
        return false;
    }
}
