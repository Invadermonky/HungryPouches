package com.invadermonky.hungrypouches.handlers;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchCrop;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchMob;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchOre;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

@Config(modid = HungryPouches.MOD_ID)
public class ConfigHandlerHP {
    @LangKey("config." + HungryPouches.MOD_ID + ".enchantments")
    @Comment("Hungry Pouch enchantment configuration.")
    public static final EnchantsConfig HUNGRY_POUCH_ENCHANTS = new EnchantsConfig();

    @LangKey("config." + HungryPouches.MOD_ID + ".pouch_crop")
    @Comment("Crop Hungry Pouch configuration.")
    public static final PouchConfig CROP_POUCH = new PouchConfig(true, ReferencesHP.CROP_POUCH_ITEMS_DEFAULTS);

    @LangKey("config." + HungryPouches.MOD_ID + ".pouch_mob")
    @Comment("Mob Hungry Pouch configuration.")
    public static final PouchConfig MOB_POUCH = new PouchConfig(true, ReferencesHP.MOB_POUCH_ITEMS_DEFAULTS);

    @LangKey("config." + HungryPouches.MOD_ID + ".pouch_ore")
    @Comment("Ore Hungry Pouch configuration.")
    public static final PouchConfig ORE_POUCH = new PouchConfig(true, ReferencesHP.ORE_POUCH_ITEMS_DEFAULTS);

    @LangKey("config." + HungryPouches.MOD_ID + ".pouch_void")
    @Comment("Void Hungry Pouch configuration.")
    public static final VoidPouchConfig VOID_POUCH = new VoidPouchConfig(true);

    @LangKey("config." + HungryPouches.MOD_ID + ".pouch_skeletal")
    @Comment("Skeletal Hungry Pouch configuration.")
    public static final SkeletalPouchConfig SKELETAL_POUCH = new SkeletalPouchConfig();

    @LangKey("config." + HungryPouches.MOD_ID + ".general")
    @Comment("General Hungry Pouch settings.")
    public static final Settings GENERAL_SETTINGS = new Settings();

    public static class Settings {
        @Comment("Hungry Pouches will bite the player if they attempt to access the inventory while the pouch is active.")
        public boolean enableBiting = true;

        @Comment("The amount of damage dealt by Hungry Pouch biting. Setting this to 0 will disable the damage but still cause the bite sound.")
        public float biteDamage = 1.0f;

        @Comment("Replaces the default item pickup noise with a chomping sound whenever an item is eaten by a Hungry Pouch.")
        public boolean enableChompPickup = true;

        @Comment("Enables usage tooltips for Hungry Pouches. These are generally \"How to...\" style tooltips.")
        public boolean enableUsageTooltips = true;

        @Comment("Enables vomit sound when ejecting items from Hungry Pouches. This only occurs when the number of items exceeds the pouch size, such as when pouch enchantments are removed.")
        public boolean enableVomitSound = true;
    }

    public static class EnchantsConfig {
        @LangKey("enchantment." + HungryPouches.MOD_ID + ".gluttonous")
        @Comment("Skeletal Hungry Pouch configuration.")
        public GluttonousConfig GLUTTONOUS = new GluttonousConfig();

        @LangKey("enchantment." + HungryPouches.MOD_ID + ".insatiable")
        @Comment("Skeletal Hungry Pouch configuration.")
        public InsatiableConfig INSATIABLE = new InsatiableConfig();

        public static class GluttonousConfig {
            @RequiresMcRestart
            @SlidingOption
            @RangeInt(min = 0, max = 3)
            @Comment("Sets the max level of the Gluttonous enchant. This enchant increases the number of slots in Hungry Pouches. A value of 0 will disable this enchant.")
            public int gluttonousMaxLevel = 3;

            @RequiresMcRestart
            @SlidingOption
            @RangeInt(min = 0, max = 3)
            @Comment("If the Gluttonous enchant is disabled this allows you to set the default Hungry Pouch bag size.\n"
                    + "  0 = 9 slots\n  1 = 12 slots\n  2 = 15 slots\n  3 = 18 slots")
            public int pouchFallbackSize = 0;
        }

        public static class InsatiableConfig {
            @RequiresMcRestart
            @SlidingOption
            @RangeInt(min = 0, max = 3)
            @Comment("Sets the max level of the Insatiable enchant. This enchant increases the stack sizes within Hungry Pouches. A value of 0 will disable this enchant.")
            public int insatiableMaxLevel = 3;

            @Comment("This determines whether the Insatiable enchant will use an exponential or geometric formula for calculating max stack size in hungry pouches.\n Exponential Formula: newSize = stackSize * (2 ^ enchLevel)\n Geometric Formula: newSize = stackSize * (1 + enchLevel)")
            public boolean exponentialInsatiable = true;
        }
    }

    public static class PouchConfig {
        @RequiresMcRestart
        @Comment("Enables this specific Hungry Pouch.")
        public boolean enablePouch;

        @LangKey("config." + HungryPouches.MOD_ID + ".item_whitelist")
        @Comment("List of items the activated Hungry Pouch will attempt to eat.\nExamples:\n  ore=ingotIron\n  type=ingot\n  item=minecraft:iron_ingot\n  item=minecraft:iron_ingot:0")
        public String[] itemWhitelist;

        public PouchConfig(boolean enablePouch, String[] itemWhitelist) {
            this.enablePouch = enablePouch;
            this.itemWhitelist = itemWhitelist;
        }
    }

    public static class VoidPouchConfig {
        @RequiresMcRestart
        @Comment("Enables this specific Hungry Pouch.")
        public boolean enablePouch;

        @Comment("Replaces the default pickup noise with a lava sizzle/extinguish sound whenever the Void Pouch consumes an item.")
        public boolean enableSizzlePickup = true;

        public VoidPouchConfig(boolean enablePouch) {
            this.enablePouch = enablePouch;
        }
    }

    public static class SkeletalPouchConfig {
        @RequiresMcRestart
        @Comment("Enables the Skeletal Hungry Pouch. This pouch stores one of each Hungry Pouch and will mimic their auto-eat functionality.")
        public boolean enableSkeletalPouch = true;

        @Comment("Replaces the default pickup noise with a skeleton rattle whenever the Skeletal Pouch eats an item.")
        public boolean enableRattlePickup = true;
    }

    @Mod.EventBusSubscriber(modid = HungryPouches.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(HungryPouches.MOD_ID)) {
                ConfigManager.sync(HungryPouches.MOD_ID, Type.INSTANCE);
                syncConfigValues();
            }
        }

        public static void syncConfigValues() {
            final String ITEM = "^item=";
            final String ORE = "^ore=";
            final String TYPE = "^type=";

            //Crop Pouch
            ItemPouchCrop.clearWhitelists();
            ItemPouchCrop.itemWhitelist.addAll(getStringsFromArray(ConfigHandlerHP.CROP_POUCH.itemWhitelist, ITEM));
            ItemPouchCrop.oreWhitelist.addAll(getOreIdsFromArray(ConfigHandlerHP.CROP_POUCH.itemWhitelist, ORE));
            ItemPouchCrop.oreWhitelist.addAll(getOreIdsFromArray(ConfigHandlerHP.CROP_POUCH.itemWhitelist, TYPE));

            //Mob Pouch
            ItemPouchMob.clearWhitelists();
            ItemPouchMob.itemWhitelist.addAll(getStringsFromArray(ConfigHandlerHP.MOB_POUCH.itemWhitelist, ITEM));
            ItemPouchMob.oreWhitelist.addAll(getOreIdsFromArray(ConfigHandlerHP.MOB_POUCH.itemWhitelist, ORE));
            ItemPouchMob.oreWhitelist.addAll(getOreIdsFromArray(ConfigHandlerHP.MOB_POUCH.itemWhitelist, TYPE));

            //Ore Pouch
            ItemPouchOre.clearWhitelists();
            ItemPouchOre.itemWhitelist.addAll(getStringsFromArray(ConfigHandlerHP.ORE_POUCH.itemWhitelist, ITEM));
            ItemPouchOre.oreWhitelist.addAll(getOreIdsFromArray(ConfigHandlerHP.ORE_POUCH.itemWhitelist, ORE));
            ItemPouchOre.oreWhitelist.addAll(getOreIdsFromArray(ConfigHandlerHP.ORE_POUCH.itemWhitelist, TYPE));
        }

        private static THashSet<String> getStringsFromArray(String[] array, String regexType) {
            THashSet<String> set = new THashSet<>();
            for(String str : array) {
                if(str.matches(regexType + "(.+)")) {
                    set.add(str.replaceFirst(regexType, ""));
                }
            }
            return set;
        }

        private static TIntHashSet getOreIdsFromArray(String[] array, String regexType) {
            TIntHashSet set = new TIntHashSet();
            for(String str : array) {
                if(str.matches(regexType + "(.+)")) {
                    if(regexType.contains("type")) {
                        for(String oreDict : OreDictionary.getOreNames()) {
                            if(oreDict.contains(str.replaceFirst(regexType, ""))) {
                                set.add(OreDictionary.getOreID(oreDict));
                            }
                        }
                    } else {
                        set.add(OreDictionary.getOreID(str.replaceFirst(regexType, "")));
                    }
                }
            }
            return set;
        }

    }
}
