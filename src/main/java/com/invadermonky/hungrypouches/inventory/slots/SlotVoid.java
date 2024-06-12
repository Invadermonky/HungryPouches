package com.invadermonky.hungrypouches.inventory.slots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotVoid extends Slot {
    public SlotVoid(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {}

    @Nullable
    @Override
    public TextureAtlasSprite getBackgroundSprite() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/lava_still");
    }
}
