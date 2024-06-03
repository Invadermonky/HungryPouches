package com.invadermonky.hungrypouches.network;

import com.invadermonky.hungrypouches.HungryPouches;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandlerHP {
    public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(HungryPouches.MOD_ID);
    private static int id = 0;

    public static void registerMessages() {
        instance.registerMessage(MessageSlotHandlerHP.class, MessageSlotContentsHP.class, id++, Side.CLIENT);
        instance.registerMessage(MessageClickHandlerHP.class, MessageClickWindowHP.class, id++, Side.SERVER);
    }
}
