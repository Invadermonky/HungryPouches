package com.invadermonky.hungrypouches.util;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;

public class ReferencesHP {
    public static final String TAG_ENABLED = HungryPouches.MOD_ID + ":enabled";
    public static final String TAG_INVENTORY = HungryPouches.MOD_ID + ":inventory";
    public static final String TAG_ITEM = "item";
    public static final String TAG_COUNT = "count";
    public static final String TAG_RAND = "rand";
    public static final String TAG_ORE = "ore";
    public static final String TAG_META = "meta";

    public static final int GUI_ID_POUCH = 0;
    public static final int GUI_ID_VOID = 1;
    public static final int GUI_ID_SKELETAL = 2;

    public static final SpriteResourceLocationHP GUI_POUCH_SMALL = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/gui_9.png", 176, 185);
    public static final SpriteResourceLocationHP GUI_POUCH_MEDIUM = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/gui_12.png", 176, 185);
    public static final SpriteResourceLocationHP GUI_POUCH_LARGE = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/gui_15.png", 176, 185);
    public static final SpriteResourceLocationHP GUI_POUCH_HUGE = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/gui_18.png", 176, 185);
    public static final SpriteResourceLocationHP GUI_POUCH_VOID = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/gui_void.png", 176, 185);
    public static final SpriteResourceLocationHP GUI_POUCH_SKELETAL = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/gui_skeletal.png", 176, 185);
    public static final SpriteResourceLocationHP GUI_SPRITE_SHEET = new SpriteResourceLocationHP(HungryPouches.MOD_ID, "textures/gui/widgets.png", 64, 120);

    public static final GuiSpriteHP SPRITE_SLOT_CROP = new GuiSpriteHP(0,0,16,16);
    public static final GuiSpriteHP SPRITE_SLOT_MOB = new GuiSpriteHP(16,0,16,16);
    public static final GuiSpriteHP SPRITE_SLOT_ORE = new GuiSpriteHP(32,0,16,16);
    public static final GuiSpriteHP SPRITE_SLOT_VOID = new GuiSpriteHP(48,0,16,16);

    public static final GuiSpriteHP SPRITE_BUTTON = new GuiSpriteHP(0,16,8,8, true);

    public static final GuiSpriteHP SPRITE_BUTTON_ENABLE = new GuiSpriteHP(16,16,8,8);
    public static final GuiSpriteHP SPRITE_BUTTON_INVENTORY = new GuiSpriteHP(16,16,8,8);
    public static final GuiSpriteHP SPRITE_BUTTON_ORE = new GuiSpriteHP(32,16,8,8);
    public static final GuiSpriteHP SPRITE_BUTTON_META = new GuiSpriteHP(48,16,8,8);


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
