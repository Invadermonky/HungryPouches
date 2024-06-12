package com.invadermonky.hungrypouches.util;

import net.minecraft.util.ResourceLocation;

public class SpriteResourceLocationHP extends ResourceLocation {
    protected final int width;
    protected final int height;

    public SpriteResourceLocationHP(String resourceName, int width, int height) {
        super(resourceName);
        this.width = width;
        this.height = height;
    }

    public SpriteResourceLocationHP(String namespaceIn, String pathIn, int width, int height) {
        super(namespaceIn, pathIn);
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
