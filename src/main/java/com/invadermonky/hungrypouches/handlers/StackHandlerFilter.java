package com.invadermonky.hungrypouches.handlers;

import com.google.common.primitives.Ints;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class StackHandlerFilter {
    protected ItemStack stack;
    protected boolean matchMeta = false;
    protected boolean matchOre = false;

    public StackHandlerFilter(ItemStack stack) {
        this.stack = stack;
    }

    public StackHandlerFilter(ItemStack stack, boolean matchMeta, boolean matchOre) {
        this.stack = stack;
        this.matchMeta = matchMeta;
        this.matchOre = matchOre;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public boolean getMatchMeta() {
        return this.matchMeta;
    }

    public void setMatchMeta(boolean matchMeta) {
        this.matchMeta = matchMeta;
    }

    public boolean getMatchOre() {
        return this.matchOre;
    }

    public void setMatchOre(boolean matchOre) {
        this.matchOre = matchOre;
    }

    public String getItemString() {
        String itemStr = this.stack.getItem().delegate.name().toString();
        if(getMatchMeta()) {
            itemStr += ":" + this.stack.getMetadata();
        }
        return itemStr;
    }

    public int[] getOreDicts() {
        return getMatchOre() ? OreDictionary.getOreIDs(this.stack) : new int[0];
    }

    public NBTTagCompound getFilterSlotNBT() {
        NBTTagCompound slotCompound = new NBTTagCompound();
        NBTTagCompound stackCompound = new NBTTagCompound();
        this.getStack().writeToNBT(stackCompound);
        slotCompound.setTag(ReferencesHP.TAG_ITEM, stackCompound);
        slotCompound.setBoolean(ReferencesHP.TAG_META, this.matchMeta);
        slotCompound.setBoolean(ReferencesHP.TAG_ORE, this.matchOre);
        return slotCompound;
    }

    public boolean matches(ItemStack checkStack) {
        String checkName = checkStack.getItem().delegate.name().toString();
        String filterName = getItemString();
        if(filterName.equals(checkName) || filterName.equals(checkName + ":" + checkStack.getMetadata())) {
            return true;
        } else if(matchOre) {
            TIntHashSet checkOres = new TIntHashSet(Ints.asList(OreDictionary.getOreIDs(checkStack)));
            for(int oreId : getOreDicts()) {
                if(checkOres.contains(oreId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
