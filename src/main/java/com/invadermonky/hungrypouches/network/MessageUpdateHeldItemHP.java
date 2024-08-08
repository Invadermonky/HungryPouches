package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchSkeletal;
import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchVoid;
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
    public boolean filterMeta;
    public boolean filterOre;
    public ItemStack updateStack;

    public MessageUpdateHeldItemHP() {
        this.updateStack = ItemStack.EMPTY;
    }

    public MessageUpdateHeldItemHP(int windowId, short transactionId, int slotId, boolean filterMeta, boolean filterOre) {
        this(windowId, transactionId, slotId);
        this.filterMeta = filterMeta;
        this.filterOre = filterOre;
    }

    public MessageUpdateHeldItemHP(int windowId, short transactionId, int slotId) {
        this(windowId, transactionId, slotId, ItemStack.EMPTY);
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
        this.filterMeta = buf.readBoolean();
        this.filterOre = buf.readBoolean();
        this.updateStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.windowId);
        buf.writeShort(this.transactionId);
        buf.writeInt(this.slotId);
        buf.writeBoolean(this.filterMeta);
        buf.writeBoolean(this.filterOre);
        ByteBufUtils.writeItemStack(buf, this.updateStack);
    }

    public static class MessageUpdateHeldItemHandlerHP implements IMessageHandler<com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP, IMessage> {
        @Override
        public IMessage onMessage(com.invadermonky.hungrypouches.network.MessageUpdateHeldItemHP message, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().player;
            if(playerMP != null) {
                playerMP.getServerWorld().addScheduledTask(() -> {
                    playerMP.markPlayerActive();
                    if(playerMP.openContainer instanceof ContainerPouchSkeletal) {
                        ContainerPouchSkeletal container = (ContainerPouchSkeletal) playerMP.openContainer;
                        if (container.windowId == message.windowId && container.getCanCraft(playerMP)) {
                            ItemStack slotStack = container.getContainerWrapper().getStackInSlot(message.slotId);
                            if(!ItemStack.areItemStacksEqualUsingNBTShareTag(slotStack, message.updateStack)) {
                                container.getContainerWrapper().setInventorySlotContents(message.slotId, message.updateStack);
                            }
                            container.detectAndSendChanges();
                        }
                    } else if(playerMP.openContainer instanceof ContainerPouchVoid && message.windowId == playerMP.openContainer.windowId) {
                        ContainerPouchVoid container = (ContainerPouchVoid) playerMP.openContainer;
                        container.getContainerWrapper().setSlotMatchMeta(message.slotId, message.filterMeta);
                        container.getContainerWrapper().setSlotMatchOre(message.slotId, message.filterOre);
                        container.getContainerWrapper().markDirty();
                    }
                });
            }
            return null;
        }
    }
}
