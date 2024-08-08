package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchHungry;
import com.invadermonky.hungrypouches.util.NetworkHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class MessageSlotContentsHP implements IMessage {
    public int windowId = 0;
    public int slot = 0;
    public ItemStack stack;

    public MessageSlotContentsHP() {
        this.stack = ItemStack.EMPTY;
    }

    public MessageSlotContentsHP(int windowId, int slot, ItemStack stack) {
        this.windowId = windowId;
        this.slot = slot;
        this.stack = stack.copy();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.windowId = buf.readByte();
        this.slot = buf.readInt();
        try {
            this.stack = NetworkHelper.readExtendedItemStack(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.windowId);
        buf.writeInt(this.slot);
        NetworkHelper.writeExtendedItemStack(buf, this.stack);
    }

    public static class MessageSlotHandlerHP implements IMessageHandler<MessageSlotContentsHP, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageSlotContentsHP message, MessageContext ctx) {
            Minecraft mc = FMLClientHandler.instance().getClient();
            EntityPlayer player = mc.player;
            if (player != null) {
                mc.addScheduledTask(() -> {
                    if (player.openContainer instanceof ContainerPouchHungry && message.windowId == player.openContainer.windowId) {
                        (player.openContainer.inventorySlots.get(message.slot)).putStack(message.stack);
                    }
                });
            }
            return null;
        }
    }
}
