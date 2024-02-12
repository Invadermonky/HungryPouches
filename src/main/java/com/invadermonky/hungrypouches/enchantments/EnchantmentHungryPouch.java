package com.invadermonky.hungrypouches.enchantments;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.common.util.EnumHelper;

public class EnchantmentHungryPouch extends Enchantment {
    public static EnumEnchantmentType TYPE_HUNGRY_POUCH;
    static {
        TYPE_HUNGRY_POUCH = EnumHelper.addEnchantmentType("hungry_pouch", item -> item instanceof AbstractPouchHP);
    }

    protected int maxLevel;

    public EnchantmentHungryPouch(String unlocName, Rarity rarity, int maxLevel) {
        super(rarity, TYPE_HUNGRY_POUCH, EntityEquipmentSlot.values());
        setRegistryName(HungryPouches.MOD_ID + ":" + unlocName);
        setName(HungryPouches.MOD_ID + ":" + unlocName);
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return (30 / maxLevel) + (enchantmentLevel - 1) * 6;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
}
