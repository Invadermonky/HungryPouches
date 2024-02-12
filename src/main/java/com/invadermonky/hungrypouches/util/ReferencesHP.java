package com.invadermonky.hungrypouches.util;

import com.invadermonky.hungrypouches.HungryPouches;
import net.minecraft.util.ResourceLocation;

public class ReferencesHP {
    public static final String TAG_ENABLED = HungryPouches.MOD_ID + ":enabled";
    public static final String TAG_INVENTORY = HungryPouches.MOD_ID + ":inventory";
    public static final String TAG_ITEM = "item";
    public static final String TAG_COUNT = "count";

    public static final ResourceLocation GUI_POUCH_SMALL = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/gui_9.png");
    public static final ResourceLocation GUI_POUCH_MEDIUM = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/gui_12.png");
    public static final ResourceLocation GUI_POUCH_LARGE = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/gui_15.png");
    public static final ResourceLocation GUI_POUCH_HUGE = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/gui_18.png");
    public static final ResourceLocation GUI_POUCH_VOID = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/gui_void.png");
    public static final ResourceLocation GUI_POUCH_SKELETAL = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/gui_skeletal.png");
    public static final ResourceLocation GUI_WIDGET_SHEET = new ResourceLocation(HungryPouches.MOD_ID, "textures/gui/widgets.png");

    //Configuration Defaults
    public static final String[] CROP_POUCH_ITEMS_DEFAULTS = new String[] {
            "ore=treeSapling",
            "type=crop",
            "type=seed",
    };

    public static final String[] MOB_POUCH_ITEMS_DEFAULTS = new String[] {
            "item=minecraft:arrow",
            "item=minecraft:blaze_rod",
            "item=minecraft:bone",
            "item=minecraft:ender_pearl",
            "item=minecraft:feather",
            "item=minecraft:ghast_tear",
            "item=minecraft:gunpowder",
            "item=minecraft:leather",
            "item=minecraft:rotten_flesh",
            "item=minecraft:slime_ball",
            "item=minecraft:skull",
            "item=minecraft:spider_eye",
            "item=minecraft:string",
            "item=minecraft:web",
            "item=thaumcraft:brain",
            "item=xreliquary:mob_ingredient"
    };

    public static final String[] ORE_POUCH_ITEMS_DEFAULTS = new String[] {
            "ore=dustRedstone",
            "type=gem",
            "type=ore"
    };

    public static final String[] VOID_POUCH_ITEMS_DEFAULTS = new String[] {
            "ore=cobblestone",
            "ore=netherrack"
    };
}
