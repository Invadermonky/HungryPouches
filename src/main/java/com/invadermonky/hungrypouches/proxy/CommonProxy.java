package com.invadermonky.hungrypouches.proxy;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.client.GuiHandlerHP;
import com.invadermonky.hungrypouches.events.EventItemPickup;
import com.invadermonky.hungrypouches.network.PacketHandlerHP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandlerHP.registerMessages();
        MinecraftForge.EVENT_BUS.register(EventItemPickup.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(HungryPouches.INSTANCE, new GuiHandlerHP());
    }
    public void init(FMLInitializationEvent event) {}
    public void postInit(FMLPostInitializationEvent event) {}
}
