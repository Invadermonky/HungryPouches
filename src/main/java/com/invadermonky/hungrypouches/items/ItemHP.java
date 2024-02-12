package com.invadermonky.hungrypouches.items;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.util.HolidayHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemHP extends Item {
    public ItemHP(String unlocName) {
        setRegistryName(unlocName);
        setTranslationKey(new ResourceLocation(HungryPouches.MOD_ID, unlocName).toString());
        setCreativeTab(CreativeTabs.MISC);

        addPropertyOverride(new ResourceLocation(HungryPouches.MOD_ID, "april_1"), (itemStack, world, entityLivingBase) -> HolidayHelper.isAprilFools() ? 1 : 0);
        addPropertyOverride(new ResourceLocation(HungryPouches.MOD_ID, "xmas"), (itemStack, world, entityLivingBase) -> HolidayHelper.isChristmas() ? 1 : 0);
    }

    public ItemHP(String unlocName, int maxStackSize) {
        this(unlocName);
        setMaxStackSize(maxStackSize);
    }
}
