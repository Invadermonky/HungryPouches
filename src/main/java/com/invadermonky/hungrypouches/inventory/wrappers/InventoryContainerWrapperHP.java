package com.invadermonky.hungrypouches.inventory.wrappers;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.handlers.StackHandlerPouch;
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

public class InventoryContainerWrapperHP implements IInventory {
    protected final ItemStack pouchStack;
    protected NBTTagCompound tagCompound;
    protected ItemStack[] inventory;

    public InventoryContainerWrapperHP(ItemStack pouch) {
        this.pouchStack = pouch;
        this.inventory = new ItemStack[this.getSizeInventory()];
        Arrays.fill(this.inventory, ItemStack.EMPTY);
        this.loadInventory();
        this.markDirty();
    }

    public ItemStack getContainerStack() {
        this.saveStacks();
        return this.pouchStack;
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
        PouchHandler.getPouchContents(pouchStack).forEach((k,v) -> this.inventory[k] = v.getStack());
    }

    protected void saveStacks() {
        TreeMap<Integer, StackHandlerPouch> contents = new TreeMap<>();
        for(int i = 0; i < this.inventory.length; i++) {
            if(!this.inventory[i].isEmpty()) {
                contents.put(i, new StackHandlerPouch(this.inventory[i]));
            }
        }
        PouchHandler.setPouchContents(this.pouchStack, contents);
    }

    @Override
    public void markDirty() {
        this.saveStacks();
    }

    @Override
    public int getSizeInventory() {
        return PouchHandler.getMaxSlots(pouchStack);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory[index];
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = this.inventory[index];
        if(stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack heldStack = stack.splitStack(count);
            if(stack.getCount() <= 0) {
                this.inventory[index] = ItemStack.EMPTY;
                heldStack.grow(stack.getCount());
            }
            return heldStack;
        }
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory[index] = stack;
        this.saveStacks();
    }

    @Override
    public int getInventoryStackLimit() {
        return PouchHandler.getMaxStackSize(this.pouchStack);
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
        return PouchHandler.isWhitelistedItem(this.pouchStack, stack);
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
    public void clear() {}

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
        return new TextComponentString(this.pouchStack.getDisplayName());
    }
}
