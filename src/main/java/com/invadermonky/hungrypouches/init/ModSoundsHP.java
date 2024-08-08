package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.HungryPouches;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSoundsHP {
    public static final SoundEvent APRIL_1_PICKUP;

    public static void registerSounds(IForgeRegistry<SoundEvent> registry) {
        registry.register(APRIL_1_PICKUP);
    }

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(HungryPouches.MOD_ID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    static {
        APRIL_1_PICKUP = makeSoundEvent("april_1_pickup");
    }
}
