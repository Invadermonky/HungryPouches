package com.invadermonky.hungrypouches.client;

import com.invadermonky.hungrypouches.client.gui.GuiHungryPouch;
import com.invadermonky.hungrypouches.client.gui.GuiSkeletalPouch;
import com.invadermonky.hungrypouches.inventory.containers.ContainerHungryPouch;
import com.invadermonky.hungrypouches.inventory.containers.ContainerSkeletalPouch;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchSkeletal;
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
                    return new ContainerHungryPouch(player.getHeldItemMainhand(), player.inventory);
                }
                break;
            case 1:
                if(player.getHeldItemMainhand().getItem() instanceof ItemPouchSkeletal) {
                    return new ContainerSkeletalPouch(player.getHeldItemMainhand(), player.inventory);
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
                    return new GuiHungryPouch(new ContainerHungryPouch(player.getHeldItemMainhand(), player.inventory), player.getHeldItemMainhand());
                }
                break;
            case 1:
                if(player.getHeldItemMainhand().getItem() instanceof ItemPouchSkeletal) {
                    return new GuiSkeletalPouch(new ContainerSkeletalPouch(player.getHeldItemMainhand(), player.inventory), player.getHeldItemMainhand());
                }
                break;
        }
        return null;
    }
}
