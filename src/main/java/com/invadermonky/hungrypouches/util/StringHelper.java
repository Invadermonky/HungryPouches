package com.invadermonky.hungrypouches.util;

import com.invadermonky.hungrypouches.HungryPouches;
import net.minecraft.item.ItemStack;

public class StringHelper {
    public static String getTranslationKey(String unloc, String type, String... params) {
        StringBuilder str = new StringBuilder(type + "." + HungryPouches.MOD_ID + ":" + unloc);
        for(String param : params) {
            str.append(".").append(param);
        }
        return str.toString();
    }

    public static String getItemId(ItemStack stack) {
        return stack.getItem().delegate.toString();
    }
}
