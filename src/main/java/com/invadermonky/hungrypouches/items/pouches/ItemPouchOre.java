package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ItemPouchOre extends AbstractPouchHP implements IHungryPouch {
    public static THashSet<String> itemWhitelist = new THashSet<>();
    public static TIntHashSet oreWhitelist = new TIntHashSet();

    public ItemPouchOre(String unlocName) {
        super(unlocName);
    }

    @Override
    public THashSet<String> getValidItems() {
        return itemWhitelist;
    }

    @Override
    public TIntHashSet getValidOres() {
        return oreWhitelist;
    }

    @Override
    public GuiSpriteHP getSkeletalSlotBackgroundIcon() {
        return ReferencesHP.SPRITE_SLOT_ORE;
    }

    public static void clearWhitelists() {
        itemWhitelist.clear();
        oreWhitelist.clear();
    }
}
