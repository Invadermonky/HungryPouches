package com.invadermonky.hungrypouches;

import com.invadermonky.hungrypouches.handlers.ConfigHandler;
import com.invadermonky.hungrypouches.proxy.CommonProxy;
import com.invadermonky.hungrypouches.util.LogHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = HungryPouches.MOD_ID,
        name = HungryPouches.MOD_NAME,
        version = HungryPouches.MOD_VERSION,
        acceptedMinecraftVersions = HungryPouches.MC_VERSION
)
public class HungryPouches {
    public static final String MOD_ID = "hungrypouches";
    public static final String MOD_NAME = "Hungry Pouches";
    public static final String MOD_VERSION = "1.12.2-0.0.3";
    public static final String MC_VERSION = "[1.12.2]";

    public static final String ProxyClientClass = "com.invadermonky.hungrypouches.proxy.ClientProxy";
    public static final String ProxyServerClass = "com.invadermonky.hungrypouches.proxy.CommonProxy";

    @Mod.Instance(MOD_ID)
    public static HungryPouches INSTANCE;

    @SidedProxy(clientSide = ProxyClientClass, serverSide = ProxyServerClass)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Starting " + MOD_NAME);
        proxy.preInit(event);
        LogHelper.debug("Finished preInit phase.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigHandler.ConfigChangeListener.syncConfigValues();
        proxy.init(event);
        LogHelper.debug("Finished init phase.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        LogHelper.debug("Finished postInit phase.");
    }
}
