package com.invadermonky.hungrypouches.inventory.containers;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.slots.SlotLocked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public abstract class ContainerCoreHP extends Container {
    protected final ItemStack pouch;
    public int pouchSlotId;

    public ContainerCoreHP(ItemStack pouch, InventoryPlayer inventory) {
        this.pouch = pouch;
        this.bindPlayerInventory(inventory);
    }

    public ItemStack getPouch() {
        return this.pouch;
    }

    protected void bindPlayerInventory(InventoryPlayer inventory) {
        int xOffset = 8;
        int yOffset = 49 + 18 * 3;

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            if(i == inventory.currentItem) {
                this.addSlotToContainer(new SlotLocked(inventory, i, xOffset + i * 18, yOffset + 58));
                this.pouchSlotId = this.inventorySlots.size() - 1;
            } else {
                this.addSlotToContainer(new Slot(inventory, i, xOffset + i * 18, yOffset + 58));
            }
        }
    }

    protected abstract void bindPouchInventory();

    protected abstract boolean performMerge(int slotIndex, ItemStack stack);

    protected ItemStack cloneStack(ItemStack stack, int count) {
        if(stack.isEmpty())
            return ItemStack.EMPTY;
        else {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            return copy;
        }
    }

    public List<IContainerListener> getListeners() {
        return this.listeners;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();
            if(!this.performMerge(index, stackInSlot)) {
                return ItemStack.EMPTY;
            }
            slot.onSlotChange(stackInSlot, stack);
            if(stackInSlot.getCount() <= 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.putStack(stackInSlot);
            }

            if(stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stackInSlot);
        }
        return stack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverse) {
        boolean success = false;
        int i = !reverse ? startIndex : endIndex - 1;
        int direction = !reverse ? 1 : -1;
        Slot slot;
        ItemStack existingStack;
        int maxStack;
        int rmv;

        if(stack.isStackable()) {
            while (stack.getCount() > 0 && ((!reverse && i < endIndex) || (reverse && i >= startIndex))) {
                slot = this.inventorySlots.get(i);

                existingStack = slot.getStack();
                if (!existingStack.isEmpty()) {

                    maxStack = PouchHandler.getMaxStackSize(this.pouch, stack, slot);
                    rmv = Math.min(maxStack, stack.getCount());

                    if (slot.isItemValid(cloneStack(stack, rmv)) && existingStack.getItem().equals(stack.getItem()) && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack)) {
                        int existingSize = existingStack.getCount() + stack.getCount();
                        if (existingSize <= maxStack) {
                            stack.setCount(0);
                            existingStack.setCount(existingSize);
                            slot.putStack(existingStack);
                            success = true;
                        } else if (existingStack.getCount() < maxStack) {
                            stack.shrink(maxStack - existingStack.getCount());
                            existingStack.setCount(maxStack);
                            slot.putStack(existingStack);
                            success = true;
                        }
                    }
                }
                i += direction;
            }
        }

        if (stack.getCount() > 0) {
            i = !reverse ? startIndex : endIndex - 1;
            while(stack.getCount() > 0 && (!reverse && i < endIndex || reverse && i >= startIndex)) {
                slot = this.inventorySlots.get(i);

                existingStack = slot.getStack();
                if (existingStack.isEmpty()) {

                    maxStack = PouchHandler.getMaxStackSize(this.pouch, stack, slot);
                    rmv = Math.min(maxStack, stack.getCount());

                    if (slot.isItemValid(cloneStack(stack, rmv))) {
                        existingStack = stack.splitStack(rmv);
                        slot.putStack(existingStack);
                        success = true;
                    }
                }
                i += direction;
            }
        }
        return success;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int j1 = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);

            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                this.resetDrag();
            }
            else if (inventoryplayer.getItemStack().isEmpty()) {
                this.resetDrag();
            }
            else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(dragType);

                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                }
                else {
                    this.resetDrag();
                }
            }
            else if (this.dragEvent == 1) {
                Slot slot7 = this.inventorySlots.get(slotId);
                ItemStack itemstack12 = inventoryplayer.getItemStack();

                if (slot7 != null && canAddItemToSlotHP(slot7, this.pouch, itemstack12, true) && slot7.isItemValid(itemstack12) && (this.dragMode == 2 || itemstack12.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    this.dragSlots.add(slot7);
                }
            }
            else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack9 = inventoryplayer.getItemStack().copy();
                    int k1 = inventoryplayer.getItemStack().getCount();

                    for (Slot slot8 : this.dragSlots) {
                        ItemStack itemstack13 = inventoryplayer.getItemStack();

                        if (slot8 != null && canAddItemToSlotHP(slot8, this.pouch, itemstack13, true) && slot8.isItemValid(itemstack13) && (this.dragMode == 2 || itemstack13.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(slot8)) {
                            ItemStack itemstack14 = itemstack9.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSizeHP(this.dragSlots, this.dragMode, this.pouch, itemstack14, j3);
                            int k3 = PouchHandler.getMaxStackSize(this.pouch, itemstack14, slot8);

                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.putStack(itemstack14);
                        }
                    }

                    itemstack9.setCount(k1);
                    inventoryplayer.setItemStack(itemstack9);
                }

                this.resetDrag();
            }
            else {
                this.resetDrag();
            }
        }
        else if (this.dragEvent != 0) {
            this.resetDrag();
        }
        else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!inventoryplayer.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
                    }
                }
            }
            else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot5 = this.inventorySlots.get(slotId);

                if (slot5 == null || !slot5.canTakeStack(player)) {
                    return ItemStack.EMPTY;
                }

                for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId)) {
                    itemstack = itemstack7.copy();
                }
            }
            else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot6 = this.inventorySlots.get(slotId);

                if (slot6 != null) {
                    ItemStack itemstack8 = slot6.getStack();
                    ItemStack itemstack11 = inventoryplayer.getItemStack();

                    if (!itemstack8.isEmpty()) {
                        itemstack = itemstack8.copy();
                    }

                    if (itemstack8.isEmpty()) {
                        if (!itemstack11.isEmpty() && slot6.isItemValid(itemstack11)) {
                            int i3 = dragType == 0 ? itemstack11.getCount() : 1;

                            if (i3 > PouchHandler.getMaxStackSize(this.pouch, itemstack11, slot6)) {
                                i3 = PouchHandler.getMaxStackSize(this.pouch, itemstack11, slot6);
                            }

                            slot6.putStack(itemstack11.splitStack(i3));
                        }
                    }
                    else if (slot6.canTakeStack(player)) {
                        if (itemstack11.isEmpty()) {
                            if (itemstack8.isEmpty()) {
                                slot6.putStack(ItemStack.EMPTY);
                                inventoryplayer.setItemStack(ItemStack.EMPTY);
                            }
                            else {
                                int l2 = dragType == 0 ? itemstack8.getCount() : (itemstack8.getCount() + 1) / 2;
                                inventoryplayer.setItemStack(slot6.decrStackSize(l2));

                                if (itemstack8.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, inventoryplayer.getItemStack());
                            }
                        }
                        else if (slot6.isItemValid(itemstack11)) {
                            if (itemstack8.getItem() == itemstack11.getItem() && itemstack8.getMetadata() == itemstack11.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11)) {
                                int k2 = dragType == 0 ? itemstack11.getCount() : 1;

                                if (k2 > PouchHandler.getMaxStackSize(this.pouch, itemstack11, slot6) - itemstack8.getCount()) {
                                    k2 = PouchHandler.getMaxStackSize(this.pouch, itemstack11, slot6) - itemstack8.getCount();
                                }

                                if(k2 > PouchHandler.getMaxStackSize(this.pouch, itemstack11) - itemstack8.getCount()) {
                                    k2 = PouchHandler.getMaxStackSize(this.pouch, itemstack11) - itemstack8.getCount();
                                }

                                itemstack11.shrink(k2);
                                itemstack8.grow(k2);
                            }
                            else if (itemstack11.getCount() <= PouchHandler.getMaxStackSize(this.pouch, itemstack11, slot6)) {
                                slot6.putStack(itemstack11);
                                inventoryplayer.setItemStack(itemstack8);
                            }
                        }
                        else if (itemstack8.getItem() == itemstack11.getItem() && itemstack11.getMaxStackSize() > 1 && (!itemstack8.getHasSubtypes() || itemstack8.getMetadata() == itemstack11.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11) && !itemstack8.isEmpty()) {
                            int j2 = itemstack8.getCount();

                            if (j2 + itemstack11.getCount() <= itemstack11.getMaxStackSize()) {
                                itemstack11.grow(j2);
                                itemstack8 = slot6.decrStackSize(j2);

                                if (itemstack8.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, inventoryplayer.getItemStack());
                            }
                        }
                    }

                    slot6.onSlotChanged();
                }
            }
        }
        else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot4 = this.inventorySlots.get(slotId);
            ItemStack itemstack6 = inventoryplayer.getStackInSlot(dragType);
            ItemStack itemstack10 = slot4.getStack();

            if (!itemstack6.isEmpty() || !itemstack10.isEmpty()) {
                if (itemstack6.isEmpty()) {
                    if (slot4.canTakeStack(player)) {
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        //slot4.onSwapCraft(itemstack10.getCount());
                        slot4.putStack(ItemStack.EMPTY);
                        slot4.onTake(player, itemstack10);
                    }
                }
                else if (itemstack10.isEmpty()) {
                    if (slot4.isItemValid(itemstack6)) {
                        int l1 = PouchHandler.getMaxStackSize(this.pouch, itemstack6, slot4);

                        if (itemstack6.getCount() > l1) {
                            slot4.putStack(itemstack6.splitStack(l1));
                        }
                        else {
                            slot4.putStack(itemstack6);
                            inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
                        }
                    }
                }
                else if (slot4.canTakeStack(player) && slot4.isItemValid(itemstack6)) {
                    int i2 = PouchHandler.getMaxStackSize(this.pouch, itemstack6, slot4);

                    if (itemstack6.getCount() > i2) {
                        slot4.putStack(itemstack6.splitStack(i2));
                        slot4.onTake(player, itemstack10);

                        if (!inventoryplayer.addItemStackToInventory(itemstack10)) {
                            player.dropItem(itemstack10, true);
                        }
                    }
                    else {
                        slot4.putStack(itemstack6);
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot4.onTake(player, itemstack10);
                    }
                }
            }
        }
        else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = this.inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack()) {
                ItemStack itemstack5 = slot3.getStack().copy();
                itemstack5.setCount(PouchHandler.getMaxStackSize(this.pouch, itemstack5, slot3));
                inventoryplayer.setItemStack(itemstack5);
            }
        }
        else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot2 = this.inventorySlots.get(slotId);

            if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
                ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
                slot2.onTake(player, itemstack4);
                player.dropItem(itemstack4, true);
            }
        }
        else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack heldStack = inventoryplayer.getItemStack();

            if (!heldStack.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
                int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                //Loop increment/decrement
                int j = dragType == 0 ? 1 : -1;

                //Clicked slot stack size limit.
                int slotLimit = slot == null ? heldStack.getMaxStackSize() : PouchHandler.getMaxStackSize(this.pouch, heldStack, slot);
                for (int k = 0; k < 2; ++k) {
                    for (int l = i; l >= 0 && l < this.inventorySlots.size() && heldStack.getCount() < slotLimit; l += j) {
                        Slot slot1 = this.inventorySlots.get(l);
                        if (slot1.getHasStack() && canAddItemToSlotHP(slot1, this.pouch, heldStack, true) && slot1.canTakeStack(player) && this.canMergeSlot(heldStack, slot1)) {
                            ItemStack itemstack2 = slot1.getStack();

                            if (k != 0 || itemstack2.getCount() != PouchHandler.getMaxStackSize(this.pouch, itemstack2, slot1)) {
                                int i1 = Math.min(slotLimit - heldStack.getCount(), itemstack2.getCount());
                                ItemStack itemstack3 = slot1.decrStackSize(i1);
                                heldStack.grow(i1);

                                if (itemstack3.isEmpty()) {
                                    slot1.putStack(ItemStack.EMPTY);
                                }

                                slot1.onTake(player, itemstack3);
                            }
                        }
                    }
                }
            }

            this.detectAndSendChanges();
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    public static void computeStackSizeHP(Set<Slot> dragSlotsIn, int dragModeIn, ItemStack pouch, ItemStack stack, int slotStackSize) {
        switch(dragModeIn) {
            case 0:
                stack.setCount(MathHelper.floor((float) stack.getCount() / (float) dragSlotsIn.size()));
                break;
            case 1:
                stack.setCount(1);
                break;
            case 2:
                stack.setCount(PouchHandler.getMaxStackSize(pouch, stack));
                break;
        }
        stack.grow(slotStackSize);
    }

    public static boolean canAddItemToSlotHP(@Nullable Slot slotIn, ItemStack pouch, ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slotIn == null || !slotIn.getHasStack();
        if(!flag && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) {
            return slotIn.getStack().getCount() + (stackSizeMatters ? stack.getCount() : 0) <= PouchHandler.getMaxStackSize(pouch, stack, slotIn);
        } else {
            return flag;
        }
    }
}
