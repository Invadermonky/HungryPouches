package com.invadermonky.hungrypouches.inventory.containers;

import com.invadermonky.hungrypouches.handlers.ConfigHandlerHP;
import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.slots.SlotFilter;
import com.invadermonky.hungrypouches.inventory.slots.SlotVoid;
import com.invadermonky.hungrypouches.inventory.wrappers.FilterContainerWrapperHP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerVoidPouch extends ContainerCoreHP {
    protected final FilterContainerWrapperHP containerWrapper;
    protected EntityPlayer player;

    public ContainerVoidPouch(ItemStack pouch, InventoryPlayer inventory, EntityPlayer player) {
        super(pouch, inventory);
        this.containerWrapper = new FilterContainerWrapperHP(pouch);
        this.player = player;
        this.bindPouchInventory();
    }

    @Override
    protected void bindPouchInventory() {
        int xOffset = 80;
        int yOffset = 36;
        this.addSlotToContainer(new SlotVoid(this.containerWrapper, 0, xOffset, yOffset));

        //Slot index 0 is the trash slot
        for(int i = 1; i < this.containerWrapper.getSizeInventory(); i++) {
            //TODO: Add filter slots
            //this.addSlotToContainer(new SlotFilter(this.containerWrapper, i, TODO x, TODO y));
            //TODO: add filter buttons
        }
    }

    @Override
    protected boolean performMerge(int slotIndex, ItemStack stack) {
        int invPlayer = 27;
        int invFull = invPlayer + 9;
        int invItem = invFull + 1;
        if(slotIndex < invFull) {
            if(ConfigHandlerHP.VOID_POUCH.enableSizzlePickup) {
                PouchHandler.playPickupSound(this.player, this.pouch);
            }
            return this.mergeItemStack(stack, invFull, invItem,false);
        }
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        /* TODO: On close the valid items needs to be updated and the filter items need to be stored in the pouch nbt.
        ItemPouchVoid.clearWhitelists();
        for(int i = 0; i < this.inventorySlots.size(); i++) {
            if(this.getSlot(i) instanceof SlotFilter && this.getSlot(i).getHasStack()) {
                SlotFilter slot = (SlotFilter) this.getSlot(i);
                if(slot.getMatchOre()) {
                    ItemPouchVoid.oreWhitelist.addAll(slot.getOreDicts());
                }
                ItemPouchVoid.itemWhitelist.add(slot.getItemString());
            }
        }
        */
        super.onContainerClosed(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, EntityPlayer player) {
        Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
        ItemStack heldStack = player.inventory.getItemStack();

        //Play sound on item destruction.
        if(slot instanceof SlotVoid && !heldStack.isEmpty()) {
            PouchHandler.playPickupSound(player, this.pouch);
        }

        //Handles creating/removing a fake item for the filter slots.
        if(slot instanceof SlotFilter) {
            if(dragType == 2) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.putStack(heldStack.isEmpty() ? ItemStack.EMPTY : heldStack.copy());
            }
            return player.inventory.getItemStack();
        } else {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }
    }
}
