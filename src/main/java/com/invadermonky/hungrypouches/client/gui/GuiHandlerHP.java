package com.invadermonky.hungrypouches.client.gui;

import com.invadermonky.hungrypouches.client.gui.screen.GuiPouchHungry;
import com.invadermonky.hungrypouches.client.gui.screen.GuiPouchSkeletal;
import com.invadermonky.hungrypouches.client.gui.screen.GuiPouchVoid;
import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchHungry;
import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchSkeletal;
import com.invadermonky.hungrypouches.inventory.containers.ContainerPouchVoid;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchSkeletal;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchVoid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandlerHP implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                if(player.getHeldItemMainhand().getItem() instanceof IHungryPouch) {
                    return new ContainerPouchHungry(player.getHeldItemMainhand(), player.inventory);
                }
                break;
            case 1:
                if(player.getHeldItemMainhand().getItem() instanceof ItemPouchVoid) {
                    return new ContainerPouchVoid(player.getHeldItemMainhand(), player.inventory, player);
                }
                break;
            case 2:
                if(player.getHeldItemMainhand().getItem() instanceof ItemPouchSkeletal) {
                    return new ContainerPouchSkeletal(player.getHeldItemMainhand(), player.inventory);
                }
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case 0:
                if(player.getHeldItemMainhand().getItem() instanceof IHungryPouch) {
                    return new GuiPouchHungry(new ContainerPouchHungry(player.getHeldItemMainhand(), player.inventory));
                }
                break;
            case 1:
                if(player.getHeldItemMainhand().getItem() instanceof ItemPouchVoid) {
                    return new GuiPouchVoid(new ContainerPouchVoid(player.getHeldItemMainhand(), player.inventory, player), player);
                }
                break;
            case 2:
                if(player.getHeldItemMainhand().getItem() instanceof ItemPouchSkeletal) {
                    return new GuiPouchSkeletal(new ContainerPouchSkeletal(player.getHeldItemMainhand(), player.inventory));
                }
                break;
        }
        return null;
    }
}
