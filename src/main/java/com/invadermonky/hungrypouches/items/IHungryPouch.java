package com.invadermonky.hungrypouches.items;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public interface IHungryPouch {
    THashSet<String> getValidItems();
    TIntHashSet getValidOres();
    GuiSpriteHP getSkeletalSlotBackgroundIcon();
}
