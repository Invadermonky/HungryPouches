package com.invadermonky.hungrypouches.items;

import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public interface IHungryPouch {
    THashSet<String> getValidItems();
    TIntHashSet getValidOres();
}
