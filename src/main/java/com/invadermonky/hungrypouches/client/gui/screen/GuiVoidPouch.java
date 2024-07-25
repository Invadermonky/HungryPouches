package com.invadermonky.hungrypouches.client.gui.screen;

import com.invadermonky.hungrypouches.inventory.containers.ContainerVoidPouch;
import com.invadermonky.hungrypouches.inventory.slots.SlotFilter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class GuiVoidPouch extends GuiCoreHP {
    protected EntityPlayer player;
    public GuiVoidPouch(ContainerVoidPouch inventorySlotsIn, EntityPlayer player) {
        super(inventorySlotsIn);
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = -1;
        for(Slot slot : this.inventorySlots.inventorySlots) {
            if(slot instanceof SlotFilter) {
                int xOffset = this.guiLeft + slot.xPos - 1;
                int yOffset = this.guiTop + slot.yPos + 17;
                //TODO: Check these buttons.
                // this.addButton(new GuiButtonFilterHP((SlotFilter) slot, ReferencesHP.SPRITE_BUTTON, true, i++, xOffset, yOffset));
                // this.addButton(new GuiButtonFilterHP((SlotFilter) slot, ReferencesHP.SPRITE_BUTTON, false, i++, xOffset + 9, yOffset));
            }
        }
    }

    @Override
    public void drawSlot(Slot slotIn) {
        super.drawSlot(slotIn);
        //TODO: Draw filter icon over filter slots.
    }
}
