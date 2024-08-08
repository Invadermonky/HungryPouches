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

public class ContainerPouchVoid extends ContainerCoreHP {
    protected final FilterContainerWrapperHP containerWrapper;
    protected EntityPlayer player;

    public ContainerPouchVoid(ItemStack pouch, InventoryPlayer inventory, EntityPlayer player) {
        super(pouch, inventory);
        this.containerWrapper = new FilterContainerWrapperHP(pouch);
        this.player = player;
        this.bindPouchInventory();
    }

    public FilterContainerWrapperHP getContainerWrapper() {
        return this.containerWrapper;
    }

    @Override
    protected void bindPouchInventory() {
        int xOffset = 80;
        int yOffset = 56;
        this.addSlotToContainer(new SlotVoid(this.containerWrapper, 0, xOffset, yOffset));

        //Slot index 0 is the trash slot
        for(int i = 1; i < this.containerWrapper.getSizeInventory(); i++) {
            this.addSlotToContainer(new SlotFilter(this.containerWrapper, i, xOffset - 88 + (i * 22), yOffset - 36));
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
            ((SlotFilter) slot).setMatchMeta(false);
            ((SlotFilter) slot).setMatchOre(false);
            return player.inventory.getItemStack();
        } else {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }
    }
}
