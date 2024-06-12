package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ItemPouchMob extends AbstractPouchHP implements IHungryPouch {
    public static THashSet<String> itemWhitelist = new THashSet<>();
    public static TIntHashSet oreWhitelist = new TIntHashSet();

    public ItemPouchMob(String unlocName) {
        super(unlocName);
    }

    @Override
    public THashSet<String> getValidItems() {
        return itemWhitelist.isEmpty() ? (itemWhitelist = new THashSet<>()) : itemWhitelist;
    }

    @Override
    public TIntHashSet getValidOres() {
        return oreWhitelist.isEmpty() ? (oreWhitelist = new TIntHashSet()) : oreWhitelist;
    }

    @Override
    public GuiSpriteHP getSkeletalSlotBackgroundIcon() {
        return ReferencesHP.SPRITE_SLOT_MOB;
    }

    public static void clearWhitelists() {
        itemWhitelist.clear();
        oreWhitelist.clear();
    }
}
