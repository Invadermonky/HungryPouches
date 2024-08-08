package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.handlers.ConfigHandlerHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.ItemHP;
import com.invadermonky.hungrypouches.items.pouches.*;
import com.invadermonky.hungrypouches.util.LogHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class ModItemsHP {
    public static final ItemHP HUNGERING_CORE;
    public static final ItemPouchCrop POUCH_CROP;
    public static final ItemPouchMob POUCH_MOB;
    public static final ItemPouchOre POUCH_ORE;
    public static final ItemPouchVoid POUCH_VOID;
    public static final ItemPouchSkeletal POUCH_SKELETAL;

    public static ArrayList<AbstractPouchHP> pouches = new ArrayList<>();

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(HUNGERING_CORE);

        if(ConfigHandlerHP.CROP_POUCH.enablePouch) {
            addPouchToRegister(POUCH_CROP);
        }

        if(ConfigHandlerHP.MOB_POUCH.enablePouch) {
            addPouchToRegister(POUCH_MOB);
        }

        if(ConfigHandlerHP.ORE_POUCH.enablePouch) {
            addPouchToRegister(POUCH_ORE);
        }

        if(ConfigHandlerHP.VOID_POUCH.enablePouch) {
            addPouchToRegister(POUCH_VOID);
        }

        if(ConfigHandlerHP.SKELETAL_POUCH.enableSkeletalPouch) {
            addPouchToRegister(POUCH_SKELETAL);
        }

        for(Item pouch : pouches) {
            registry.register(pouch);
        }
        LogHelper.debug("Items registered.");
    }

    public static void registerItemModels(ModelRegistryEvent event) {
        registerItemRenders(HUNGERING_CORE);
        pouches.forEach(ModItemsHP::registerItemRenders);
        LogHelper.debug("Registered item models.");
    }

    private static void registerItemRenders(Item item) {
        ModelResourceLocation loc = new ModelResourceLocation(item.delegate.name(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }

    protected static void addPouchToRegister(AbstractPouchHP... pouchesToAdd) {
        for(AbstractPouchHP pouch : pouchesToAdd) {
            if(pouch != null)
                pouches.add(pouch);
        }
    }

    static {
        HUNGERING_CORE = new ItemHP("hungering_core", 1);
        POUCH_CROP = new ItemPouchCrop("pouch_crop");
        POUCH_MOB = new ItemPouchMob("pouch_mob");
        POUCH_ORE = new ItemPouchOre("pouch_ore");
        POUCH_VOID = new ItemPouchVoid("pouch_void");
        POUCH_SKELETAL = new ItemPouchSkeletal("pouch_skeletal");
    }
}
