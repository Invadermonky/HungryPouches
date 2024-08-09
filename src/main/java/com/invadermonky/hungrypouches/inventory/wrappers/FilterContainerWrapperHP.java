package com.invadermonky.hungrypouches.inventory.wrappers;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.handlers.StackHandlerFilter;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.TreeMap;

public class FilterContainerWrapperHP implements IInventory {
    protected final ItemStack pouchStack;
    protected NBTTagCompound tagCompound;
    protected StackHandlerFilter[] filter;

    public FilterContainerWrapperHP(ItemStack pouch) {
        this.pouchStack = pouch;
        this.filter = new StackHandlerFilter[this.getSizeInventory()];
        Arrays.fill(this.filter, new StackHandlerFilter(ItemStack.EMPTY));
        this.loadInventory();
        this.markDirty();
    }

    public boolean getSlotMatchMeta(int index) {
        return this.filter[index].getMatchMeta();
    }

    public void setSlotMatchMeta(int index, boolean matchMeta) {
        this.filter[index].setMatchMeta(matchMeta);
    }

    public String getItemString(int index) {
        return this.filter[index].getItemString();
    }

    public boolean getSlotMatchOre(int index) {
        return this.filter[index].getMatchOre();
    }

    public void setSlotMatchOre(int index, boolean matchOre) {
        this.filter[index].setMatchOre(matchOre);
    }

    public int[] getSlotOreDicts(int index) {
        return filter[index].getOreDicts();
    }

    protected void loadInventory() {
        if(!this.pouchStack.hasTagCompound() || !this.pouchStack.getTagCompound().hasKey(ReferencesHP.TAG_INVENTORY)) {
            if(this.pouchStack.hasTagCompound()) {
                if(this.pouchStack.getTagCompound().hasKey(ReferencesHP.TAG_INVENTORY)) {
                    this.tagCompound = this.pouchStack.getTagCompound().getCompoundTag(ReferencesHP.TAG_INVENTORY);
                    this.pouchStack.getTagCompound().removeTag(ReferencesHP.TAG_INVENTORY);
                } else {
                    this.tagCompound = this.pouchStack.getTagCompound();
                }
                this.loadStacks();
                this.tagCompound = new NBTTagCompound();
                this.saveStacks();
            } else {
                this.pouchStack.setTagInfo(ReferencesHP.TAG_INVENTORY, new NBTTagCompound());
            }
        }

        this.tagCompound = this.pouchStack.getTagCompound().getCompoundTag(ReferencesHP.TAG_INVENTORY);
        this.loadStacks();
    }

    protected void loadStacks() {
        PouchHandler.getFilterContents(this.pouchStack).forEach((k,v) -> this.filter[k] = v);
    }

    protected void saveStacks() {
        TreeMap<Integer, StackHandlerFilter> contents = new TreeMap<>();
        for(int i = 0; i < this.filter.length; i++) {
            if(!this.filter[i].isEmpty()) {
                contents.put(i, filter[i]);
            }
        }
        PouchHandler.setFilterContents(this.pouchStack, contents);
    }

    @Override
    public int getSizeInventory() {
        return PouchHandler.getMaxSlots(this.pouchStack);
    }

    @Override
    public boolean isEmpty() {
        for(StackHandlerFilter slotHandler : this.filter) {
            if(!slotHandler.getStack().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        return this.filter[index].getStack();
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(count > 0) {
            this.filter[index].setStack(ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.decrStackSize(index, 1);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.filter[index] = new StackHandlerFilter(stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        this.saveStacks();
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {}

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        this.markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        Arrays.fill(this.filter, new StackHandlerFilter(ItemStack.EMPTY));
    }

    @Nonnull
    @Override
    public String getName() {
        return this.pouchStack.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return this.pouchStack.hasDisplayName();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }
}
