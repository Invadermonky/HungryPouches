package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.handlers.ConfigHandlerHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.ItemHP;
import com.invadermonky.hungrypouches.items.pouches.*;
import com.invadermonky.hungrypouches.util.LogHelper;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ItemRegistryHP {
    public static Item hungeringCore;
    public static AbstractPouchHP cropPouch, mobPouch, orePouch, voidPouch, skeletalPouch;

    public static ArrayList<AbstractPouchHP> pouches = new ArrayList<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        hungeringCore = new ItemHP("hungering_core", 1);
        registry.register(hungeringCore);

        if(ConfigHandlerHP.CROP_POUCH.enablePouch) {
            cropPouch = new ItemPouchCrop("pouch_crop");
            addPouchToRegister(cropPouch);
        }

        if(ConfigHandlerHP.MOB_POUCH.enablePouch) {
            mobPouch = new ItemPouchMob("pouch_mob");
            addPouchToRegister(mobPouch);
        }

        if(ConfigHandlerHP.ORE_POUCH.enablePouch) {
            orePouch = new ItemPouchOre("pouch_ore");
            addPouchToRegister(orePouch);
        }

        if(ConfigHandlerHP.VOID_POUCH.enablePouch) {
            voidPouch = new ItemPouchVoid("pouch_void");
            addPouchToRegister(voidPouch);
        }

        if(ConfigHandlerHP.SKELETAL_POUCH.enableSkeletalPouch) {
            skeletalPouch = new ItemPouchSkeletal("pouch_skeletal");
            addPouchToRegister(skeletalPouch);
        }

        for(Item pouch : pouches) {
            registry.register(pouch);
        }
        LogHelper.debug("Items registered.");
    }

    protected static void addPouchToRegister(AbstractPouchHP... pouchesToAdd) {
        for(AbstractPouchHP pouch : pouchesToAdd) {
            if(pouch != null)
                pouches.add(pouch);
        }
    }
}
