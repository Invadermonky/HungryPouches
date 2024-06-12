package com.invadermonky.hungrypouches.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.io.IOException;

public class NetworkHelper {
    public static void writeNBT(ByteBuf buf, @Nullable NBTTagCompound tagCompound) {
        if(tagCompound == null) {
            buf.writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(tagCompound, new ByteBufOutputStream(buf));
            } catch (IOException e) {
                throw new EncoderException(e);
            }
        }
    }

    public static NBTTagCompound readNBT(ByteBuf buf) {
        int i = buf.readerIndex();
        byte b0 = buf.readByte();
        if(b0 == 0) {
            return null;
        } else {
            buf.readerIndex(i);
            try {
                return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
            } catch (IOException e) {
                throw new EncoderException(e);
            }
        }
    }

    public static void writeExtendedItemStack(ByteBuf buf, ItemStack stack) {
        if(stack.isEmpty()) {
            buf.writeShort(-1);
        } else {
            buf.writeShort(Item.getIdFromItem(stack.getItem()));
            buf.writeInt(stack.getCount());
            buf.writeShort(stack.getMetadata());
            NBTTagCompound tagCompound = null;
            if(stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                tagCompound = stack.getItem().getNBTShareTag(stack);
            }
            writeNBT(buf, tagCompound);
        }
    }

    public static void writeExtendedItemStack(PacketBuffer buf, ItemStack stack) {
        if(stack.isEmpty()) {
            buf.writeShort(-1);
        } else {
            buf.writeShort(Item.getIdFromItem(stack.getItem()));
            buf.writeInt(stack.getCount());
            buf.writeShort(stack.getMetadata());
            NBTTagCompound tagCompound = null;
            if(stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                tagCompound = stack.getTagCompound();
            }
            buf.writeCompoundTag(tagCompound);
        }
    }

    public static ItemStack readExtendedItemStack(ByteBuf buf) throws IOException {
        int itemId = buf.readShort();
        if(itemId < 0) {
            return ItemStack.EMPTY;
        } else {
            int count = buf.readInt();
            int meta = buf.readShort();
            ItemStack stack = new ItemStack(Item.getItemById(itemId), count, meta);
            stack.setTagCompound(readNBT(buf));
            return stack;
        }
    }

    public static ItemStack readExtendedItemStack(PacketBuffer buf) throws IOException {
        int itemId = buf.readShort();
        if(itemId < 0) {
            return ItemStack.EMPTY;
        } else {
            int count = buf.readInt();
            int meta = buf.readShort();
            ItemStack stack = new ItemStack(Item.getItemById(itemId), count, meta);
            stack.setTagCompound(buf.readCompoundTag());
            return stack;
        }
    }
}
