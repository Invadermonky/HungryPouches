package com.invadermonky.hungrypouches.events;

import com.invadermonky.hungrypouches.inventory.containers.ContainerCoreHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventItemPickup {
    public static final EventItemPickup INSTANCE = new EventItemPickup();

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();

        if(player.openContainer instanceof ContainerCoreHP) {
            return;
        }

        InventoryPlayer inventory = player.inventory;
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.getItem() instanceof AbstractPouchHP) {
                if(((AbstractPouchHP) stack.getItem()).onItemPickup(event, stack)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}
