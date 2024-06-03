package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.inventory.containers.ContainerHungryPouch;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageClickHandlerHP implements IMessageHandler<MessageClickWindowHP, IMessage> {
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
                    } else if(container instanceof ContainerHungryPouch) {
                        ((ContainerHungryPouch) container).syncInventory(playerMP);
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
