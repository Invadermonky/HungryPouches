package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.inventory.containers.ContainerHungryPouch;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSlotHandlerHP implements IMessageHandler<MessageSlotContentsHP, IMessage> {
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(MessageSlotContentsHP message, MessageContext ctx) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        EntityPlayer player = mc.player;
        if (player != null) {
            mc.addScheduledTask(() -> {
                if (player.openContainer instanceof ContainerHungryPouch && message.windowId == player.openContainer.windowId) {
                    (player.openContainer.inventorySlots.get(message.slot)).putStack(message.stack);
                }
            });
        }
        return null;
    }
}
