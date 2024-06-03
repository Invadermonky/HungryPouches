package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.util.StringHelper;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Objects;

public class ItemPouchVoid extends AbstractPouchHP implements IHungryPouch {
    public static THashSet<String> itemWhitelist = new THashSet<>();
    public static TIntHashSet oreWhitelist = new TIntHashSet();

    public ItemPouchVoid(String unlocName) {
        super(unlocName);
    }

    @Override
    public int getMaxSlots(ItemStack pouch) {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
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

    @Override
    protected void addDefaultTooltip(ItemStack stack, List<String> tooltip) {
        if(PouchHandler.isEnabled(stack))
            tooltip.add(I18n.format(StringHelper.getLanguageKey("enabled", "tooltip")));
        else
            tooltip.add(I18n.format(StringHelper.getLanguageKey("disabled", "tooltip")));
    }

    @Override
    protected void addNoKeyTooltip(ItemStack stack, List<String> tooltip) {}

    @Override
    protected void addShiftTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getLanguageKey(Objects.requireNonNull(getRegistryName()).getPath() + ".desc", "tooltip")));
        //TODO: Add tooltip for accepted items.
        //TODO: Maybe change the GUI to have a filter instead of a configured item pickup.
    }

    @Override
    protected void addCtrlTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getLanguageKey("enableinfo", "tooltip")));
    }
}
