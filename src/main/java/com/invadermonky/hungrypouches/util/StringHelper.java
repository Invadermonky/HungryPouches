package com.invadermonky.hungrypouches.util;

import com.invadermonky.hungrypouches.HungryPouches;

public class StringHelper {
    public static String getLanguageKey(String unlocalizedStr, String type) {
        return String.format("%s.%s:%s", type, HungryPouches.MOD_ID, unlocalizedStr);
    }
}
