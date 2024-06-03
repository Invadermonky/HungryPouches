package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.util.NetworkHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
}
