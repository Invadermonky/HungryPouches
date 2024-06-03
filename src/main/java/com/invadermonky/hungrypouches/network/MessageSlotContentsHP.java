package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.util.NetworkHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
}
