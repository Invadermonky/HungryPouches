package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
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
        return itemWhitelist.isEmpty() ? (itemWhitelist = new THashSet<>()) : itemWhitelist;
    }

    @Override
    public TIntHashSet getValidOres() {
        return oreWhitelist.isEmpty() ? (oreWhitelist = new TIntHashSet()) : oreWhitelist;
    }

    public static void clearWhitelists() {
        itemWhitelist.clear();
        oreWhitelist.clear();
    }
}
