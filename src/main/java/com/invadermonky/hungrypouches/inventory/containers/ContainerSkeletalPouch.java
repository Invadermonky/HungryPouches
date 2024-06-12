package com.invadermonky.hungrypouches.inventory.containers;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.slots.SlotSkeletalPouch;
import com.invadermonky.hungrypouches.inventory.wrappers.InventoryContainerWrapperHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchSkeletal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerSkeletalPouch extends ContainerCoreHP {
    protected final InventoryContainerWrapperHP containerWrapper;
    protected final EntityPlayer player;
    protected List<Item> registeredPouches;
    protected SlotSkeletalPouch feedSlot;

    public ContainerSkeletalPouch(ItemStack pouch, InventoryPlayer inventory) {
        super(pouch, inventory);
        this.containerWrapper = new InventoryContainerWrapperHP(pouch);
        this.player = inventory.player;
        this.registeredPouches = ItemPouchSkeletal.getRegisteredPouches();
        this.bindPouchInventory();
    }

    public InventoryContainerWrapperHP getContainerWrapper() {
        return this.containerWrapper;
    }

    @Override
    protected void bindPouchInventory() {
        //Width 0 - 176
        int xOffset = 8;
        int yOffset = 20;
        int spacing = (176 - (xOffset * 2) - (this.registeredPouches.size() * 18)) / (this.registeredPouches.size() + 1);
        int i;

        for(i = 0; i < registeredPouches.size(); i++) {
            this.addSlotToContainer(new SlotSkeletalPouch(this.containerWrapper, i, xOffset + (18 * i) + ((spacing + 1) * (i + 1)), yOffset + 31, this.registeredPouches.get(i)));
        }
        this.feedSlot = new SlotSkeletalPouch(this.containerWrapper, i, 79, yOffset + 1);
        this.addSlotToContainer(this.feedSlot);
    }

    @Override
    protected boolean performMerge(int slotIndex, ItemStack stack) {
        int invPlayer = 27;
        int invFull = invPlayer + 9;
        int invItem = invFull + this.containerWrapper.getSizeInventory();
        return slotIndex < invFull ? this.mergeItemStack(stack, invFull, invItem, false) : this.mergeItemStack(stack, 0, invFull, true);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(this.feedSlot.getHasStack()) {
            ItemStack feedStack = this.feedSlot.getStack();
            int oldCount = feedStack.getCount();
            for(int i = 0; i < this.containerWrapper.getSizeInventory(); i++) {
                ItemStack nestedPouch = this.containerWrapper.getStackInSlot(i);
                if(nestedPouch.getItem() instanceof AbstractPouchHP) {
                    PouchHandler.insertStackIntoPouch(this.player, nestedPouch, feedStack, true, true);
                }
                if(feedStack.isEmpty()) {
                    break;
                }
            }

            if(oldCount != feedStack.getCount()) {
                putStackInSlot(this.feedSlot.slotNumber, feedStack);
                for(IContainerListener listener : this.listeners) {
                    listener.sendSlotContents(this, this.feedSlot.slotNumber, feedStack);
                }
            }
        }
    }

    @Override
    public void onContainerClosed(@Nonnull EntityPlayer playerIn) {
        if(feedSlot.getHasStack()) {
            ItemStack feedStack = feedSlot.getStack();
            playerIn.inventory.addItemStackToInventory(feedStack);
            if(!feedStack.isEmpty()) {
                playerIn.dropItem(feedSlot.getStack(), false);
            }
            feedSlot.putStack(ItemStack.EMPTY);
        }

        super.onContainerClosed(playerIn);
    }
}
