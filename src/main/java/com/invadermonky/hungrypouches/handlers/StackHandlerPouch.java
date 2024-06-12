package com.invadermonky.hungrypouches.handlers;

import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * This implementation is wonky looking but it's a way to bypass the NBT byte primitive size to allow
 * Hungry Pouch slots to support stack sizes greater than 127.
 */
public class StackHandlerPouch {
    private ItemStack stack;
    private int count;

    public StackHandlerPouch(ItemStack stack, int count) {
        this.setStack(stack, count);
    }

    public StackHandlerPouch(ItemStack stack) {
        this.setStack(stack);
    }

    public ItemStack getStack() {
        ItemStack copy = this.stack.copy();
        copy.setCount(this.count);
        return copy;
    }

    public StackHandlerPouch setStack(ItemStack stack) {
        return this.setStack(stack, stack.getCount());
    }

    public StackHandlerPouch setStack(ItemStack stack, int count) {
        this.stack = stack.copy();
        this.stack.setCount(1);
        this.count = count;
        return this;
    }

    public ItemStack getSingleStack() {
        return this.stack;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void grow(int amount) {
        this.count += amount;
    }

    public void shrink(int amount) {
        this.count = Math.max(0, this.count - amount);
    }

    public NBTTagCompound getInventorySlotNBT() {
        NBTTagCompound slotCompound = new NBTTagCompound();
        NBTTagCompound stackCompound = new NBTTagCompound();
        this.getSingleStack().writeToNBT(stackCompound);
        slotCompound.setTag(ReferencesHP.TAG_ITEM, stackCompound);
        slotCompound.setInteger(ReferencesHP.TAG_COUNT, this.count);
        return slotCompound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackHandlerPouch that = (StackHandlerPouch) o;
        return ItemStack.areItemsEqual(stack, that.stack) && ItemStack.areItemStackTagsEqual(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }
}
