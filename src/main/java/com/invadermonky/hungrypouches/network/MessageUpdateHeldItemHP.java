package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.inventory.containers.ContainerSkeletalPouch;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateHeldItemHP implements IMessage {
    public int windowId;
    public short transactionId;
    public int slotId;
    public ItemStack updateStack;

    public MessageUpdateHeldItemHP() {
        this.updateStack = ItemStack.EMPTY;
    }

    public MessageUpdateHeldItemHP(int windowId, short transactionId, int slotId, ItemStack updateStack) {
        this.windowId = windowId;
        this.transactionId = transactionId;
        this.slotId = slotId;
        this.updateStack = updateStack.copy();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.windowId = buf.readByte();
        this.transactionId = buf.readShort();
        this.slotId = buf.readInt();
        this.updateStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.windowId);
        buf.writeShort(this.transactionId);
        buf.writeInt(this.slotId);
        ByteBufUtils.writeItemStack(buf, this.updateStack);
    }

    public static class MessageUpdateHeldItemHandlerHP implements IMessageHandler<com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP, IMessage> {
        @Override
        public IMessage onMessage(com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP message, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().player;
            if(playerMP != null) {
                playerMP.getServerWorld().addScheduledTask(() -> {
                    playerMP.markPlayerActive();
                    if(playerMP.openContainer instanceof ContainerSkeletalPouch) {
                        ContainerSkeletalPouch container = (ContainerSkeletalPouch) playerMP.openContainer;
                        if (container.windowId == message.windowId && container.getCanCraft(playerMP)) {
                            ItemStack slotStack = container.getContainerWrapper().getStackInSlot(message.slotId);
                            if(!ItemStack.areItemStacksEqualUsingNBTShareTag(slotStack, message.updateStack)) {
                                container.getContainerWrapper().setInventorySlotContents(message.slotId, message.updateStack);
                            }
                            container.detectAndSendChanges();
                        }
                    }
                });
            }
            return null;
        }
    }
}
