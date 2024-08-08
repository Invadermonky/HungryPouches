package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchHungry;
import com.invadermonky.hungrypouches.util.NetworkHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class MessageClickWindowHP implements IMessage {
    public int windowId;
    public int slot;
    public int mouseButton;
    public short transactionId;
    public ItemStack clickedItem;
    public ClickType clickType;

    public MessageClickWindowHP() {
        this.clickedItem = ItemStack.EMPTY;
    }

    public MessageClickWindowHP(int windowId, int slot, int mouseButton, short transactionId, ItemStack clickedItem, ClickType clickType) {
        this.windowId = windowId;
        this.slot = slot;
        this.mouseButton = mouseButton;
        this.transactionId = transactionId;
        this.clickedItem = clickedItem.copy();
        this.clickType = clickType;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.windowId = buf.readByte();
        this.slot = buf.readInt();
        this.mouseButton = buf.readByte();
        this.transactionId = buf.readShort();
        this.clickType = ClickType.values()[buf.readInt()];
        try {
            this.clickedItem = NetworkHelper.readExtendedItemStack(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.windowId);
        buf.writeInt(this.slot);
        buf.writeByte(this.mouseButton);
        buf.writeShort(this.transactionId);
        buf.writeInt(this.clickType.ordinal());
        NetworkHelper.writeExtendedItemStack(buf, this.clickedItem);
    }

    public static class MessageClickHandlerHP implements IMessageHandler<MessageClickWindowHP, IMessage> {
        @Override
        public IMessage onMessage(MessageClickWindowHP message, MessageContext ctx) {
            final EntityPlayerMP playerMP = ctx.getServerHandler().player;
            if(playerMP != null) {
                playerMP.getServerWorld().addScheduledTask(() -> {
                    playerMP.markPlayerActive();
                    Container container = playerMP.openContainer;
                    if(container.windowId == message.windowId && container.getCanCraft(playerMP)) {
                        ItemStack stack = container.slotClick(message.slot, message.mouseButton, message.clickType, playerMP);
                        if(ItemStack.areItemStacksEqualUsingNBTShareTag(message.clickedItem, stack)) {
                            playerMP.connection.sendPacket(new SPacketConfirmTransaction(message.windowId, message.transactionId, true));
                            playerMP.isChangingQuantityOnly = true;
                            playerMP.openContainer.detectAndSendChanges();
                            playerMP.updateHeldItem();
                            playerMP.isChangingQuantityOnly = false;
                        } else if(container instanceof ContainerPouchHungry) {
                            ((ContainerPouchHungry) container).syncInventory(playerMP);
                        } else {
                            NonNullList<ItemStack> nonNullList = NonNullList.create();
                            for(int i = 0; i < container.inventorySlots.size(); i++) {
                                ItemStack slotStack = container.inventorySlots.get(i).getStack();
                                slotStack = slotStack.isEmpty() ? ItemStack.EMPTY : slotStack;
                                nonNullList.add(slotStack);
                            }
                            playerMP.sendAllContents(container, nonNullList);
                        }
                    }
                });
            }
            return null;
        }
    }
}
