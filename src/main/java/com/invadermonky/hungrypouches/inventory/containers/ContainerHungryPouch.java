package com.invadermonky.hungrypouches.inventory.containers;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.slots.SlotHungryPouch;
import com.invadermonky.hungrypouches.inventory.slots.SlotVoid;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchVoid;
import com.invadermonky.hungrypouches.network.MessageSlotContentsHP;
import com.invadermonky.hungrypouches.network.PacketHandlerHP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerHungryPouch extends ContainerCoreHP {
    protected final int rowSize;
    protected final boolean isVoid;

    public ContainerHungryPouch(ItemStack pouch, InventoryPlayer inventory) {
        super(pouch, inventory);
        this.rowSize = PouchHandler.getMaxSlots(this.pouch) / 3;
        this.isVoid = this.pouch.getItem() instanceof ItemPouchVoid;
        this.bindPouchInventory();
    }

    @Override
    protected void bindPouchInventory() {
        int xOffset;
        int yOffset;

        if(isVoid) {
            xOffset = 80;
            yOffset = 36;
            this.addSlotToContainer(new SlotVoid(this.containerWrapper, 0, xOffset, yOffset));
        } else {
            xOffset = (9 + 9 * 9) - (rowSize * 10);
            yOffset = 16;
            for (int i = 0; i < PouchHandler.getMaxSlots(this.pouch); i++) {
                this.addSlotToContainer(new SlotHungryPouch(this.containerWrapper, i, xOffset + i % this.rowSize * 20, yOffset + i / this.rowSize * 20));
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        for(int i = 0; i < this.inventorySlots.size(); i++) {
            ItemStack slotStack = this.inventorySlots.get(i).getStack();
            ItemStack inventoryStack = this.inventoryItemStacks.get(i);
            if(!ItemStack.areItemStacksEqual(slotStack, inventoryStack)) {
                inventoryStack = slotStack.isEmpty() ? ItemStack.EMPTY : slotStack.copy();
                this.inventoryItemStacks.set(i, inventoryStack);

                for (IContainerListener listener : this.listeners) {
                    if (listener instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) listener;
                        this.syncSlot(playerMP, i, inventoryStack);
                    }
                }
            }
        }
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        if(this.listeners.contains(listener)) {
            throw new IllegalArgumentException("Listener already listening.");
        } else {
            this.listeners.add(listener);
            if(listener instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) listener;
                this.syncInventory(playerMP);
            }
            this.detectAndSendChanges();
        }
    }

    public List<IContainerListener> getListeners() {
        return this.listeners;
    }

    public void syncInventory(EntityPlayerMP playerMP) {
        for(int i = 0; i < this.inventorySlots.size(); i++) {
            ItemStack stack = this.inventorySlots.get(i).getStack();
            PacketHandlerHP.instance.sendTo(new MessageSlotContentsHP(this.windowId, i, stack), playerMP);
        }
        playerMP.connection.sendPacket(new SPacketSetSlot(-1, -1, playerMP.inventory.getItemStack()));
    }

    public void syncSlot(EntityPlayerMP playerMP, int slot, ItemStack stack) {
        PacketHandlerHP.instance.sendTo(new MessageSlotContentsHP(this.windowId, slot, stack), playerMP);
    }
}
