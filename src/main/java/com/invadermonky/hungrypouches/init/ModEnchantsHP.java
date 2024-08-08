package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.enchantments.EnchantmentHungryPouch;
import com.invadermonky.hungrypouches.handlers.ConfigHandlerHP;
import com.invadermonky.hungrypouches.util.LogHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEnchantsHP {
    public static boolean enableGluttonousEnchant = ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.GLUTTONOUS.gluttonousMaxLevel > 0;
    public static boolean enableInsatiableEnchant = ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.INSATIABLE.insatiableMaxLevel > 0;

    public static final EnchantmentHungryPouch GLUTTONOUS;
    public static final EnchantmentHungryPouch INSATIABLE;

    public static void registerEnchants(IForgeRegistry<Enchantment> registry) {
        if(enableInsatiableEnchant) {
            registry.register(INSATIABLE);
        }

        if(enableGluttonousEnchant) {
            registry.register(GLUTTONOUS);
        }

        LogHelper.debug("Enchantments registered.");
    }

    static {
        INSATIABLE = new EnchantmentHungryPouch("insatiable", Enchantment.Rarity.RARE, ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.INSATIABLE.insatiableMaxLevel);
        GLUTTONOUS = new EnchantmentHungryPouch("gluttonous", Enchantment.Rarity.VERY_RARE, ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.GLUTTONOUS.gluttonousMaxLevel);
    }
}
