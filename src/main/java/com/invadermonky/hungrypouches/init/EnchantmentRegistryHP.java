package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.enchantments.EnchantmentHungryPouch;
import com.invadermonky.hungrypouches.handlers.ConfigHandler;
import com.invadermonky.hungrypouches.util.LogHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class EnchantmentRegistryHP {
    public static boolean enableGluttonousEnchant = ConfigHandler.HUNGRY_POUCH_ENCHANTS.GLUTTONOUS.gluttonousMaxLevel > 0;
    public static boolean enableInsatiableEnchant = ConfigHandler.HUNGRY_POUCH_ENCHANTS.INSATIABLE.insatiableMaxLevel > 0;

    public static EnchantmentHungryPouch gluttonous, insatiable;

    @SubscribeEvent
    public static void registerEnchants(RegistryEvent.Register<Enchantment> event) {
        IForgeRegistry<Enchantment> registry = event.getRegistry();

        if(enableInsatiableEnchant) {
            insatiable = new EnchantmentHungryPouch("insatiable", Enchantment.Rarity.RARE, ConfigHandler.HUNGRY_POUCH_ENCHANTS.INSATIABLE.insatiableMaxLevel);
            registry.register(insatiable);
        }

        if(enableGluttonousEnchant) {
            gluttonous = new EnchantmentHungryPouch("gluttonous", Enchantment.Rarity.VERY_RARE, ConfigHandler.HUNGRY_POUCH_ENCHANTS.GLUTTONOUS.gluttonousMaxLevel);
            registry.register(gluttonous);
        }

        LogHelper.debug("Enchantments registered.");
    }
}
