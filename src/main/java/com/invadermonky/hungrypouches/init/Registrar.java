package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.HungryPouches;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = HungryPouches.MOD_ID)
public class Registrar {
    @SubscribeEvent
    public static void registerModEnchants(RegistryEvent.Register<Enchantment> event) {
        ModEnchantsHP.registerEnchants(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModItems(RegistryEvent.Register<Item> event) {
        ModItemsHP.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModSounds(RegistryEvent.Register<SoundEvent> event) {
        ModSoundsHP.registerSounds(event.getRegistry());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModModels(ModelRegistryEvent event) {
        ModItemsHP.registerItemModels(event);
    }
}
