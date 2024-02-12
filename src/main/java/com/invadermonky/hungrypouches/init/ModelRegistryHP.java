package com.invadermonky.hungrypouches.init;

import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.util.LogHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModelRegistryHP {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerItemRenders(ModelRegistryEvent event) {

        registerItemRender(ItemRegistryHP.hungeringCore);

        for(AbstractPouchHP pouch : ItemRegistryHP.pouches) {
            registerItemRender(pouch);
        }
        LogHelper.debug("Registered item models.");
    }

    public static void registerItemRender(Item item) {
        ModelResourceLocation loc = new ModelResourceLocation(item.delegate.name(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }
}
