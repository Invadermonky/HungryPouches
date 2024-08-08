package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.client.gui.util.GuiSpriteHP;
import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.handlers.StackHandlerFilter;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import com.invadermonky.hungrypouches.util.StringHelper;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class ItemPouchVoid extends AbstractPouchHP implements IHungryPouch {
    public static THashSet<String> itemWhitelist = new THashSet<>();
    public static TIntHashSet oreWhitelist = new TIntHashSet();

    public ItemPouchVoid(String unlocName) {
        super(unlocName);
    }

    @Override
    public int getMaxSlots(ItemStack pouch) {
        //There are 7 filter slots + 1 void slot.
        return 8;
    }

    @Override
    public int getGuiId() {
        return ReferencesHP.GUI_ID_VOID;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public THashSet<String> getValidItems() {
        return itemWhitelist == null ? (itemWhitelist = new THashSet<>()) : itemWhitelist;
    }

    @Override
    public TIntHashSet getValidOres() {
        return oreWhitelist == null ? (oreWhitelist = new TIntHashSet()) : oreWhitelist;
    }

    @Override
    public GuiSpriteHP getSkeletalSlotBackgroundIcon() {
        return ReferencesHP.SPRITE_SLOT_VOID;
    }

    public static void clearWhitelists() {
        itemWhitelist.clear();
        oreWhitelist.clear();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey(ReferencesHP.TAG_RAND)) {
            stack.getTagCompound().removeTag(ReferencesHP.TAG_RAND);
        }
    }

    @Override
    public boolean onEntitySwing(@Nonnull EntityLivingBase entityLiving, @Nonnull ItemStack stack) {
        return false;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, EnumHand hand) {
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    protected void addDefaultTooltip(ItemStack stack, List<String> tooltip) {
        if(PouchHandler.isEnabled(stack))
            tooltip.add(I18n.format(StringHelper.getTranslationKey("enabled", "tooltip")));
        else
            tooltip.add(I18n.format(StringHelper.getTranslationKey("disabled", "tooltip")));
    }

    @Override
    protected void addShiftTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getTranslationKey(Objects.requireNonNull(getRegistryName()).getPath(), "tooltip", "desc")));

        TreeMap<Integer, StackHandlerFilter> filter = PouchHandler.getFilterContents(stack);
        if(filter.isEmpty()) {
            tooltip.add(I18n.format(StringHelper.getTranslationKey("empty", "tooltip")));
        } else {
            tooltip.add(I18n.format(StringHelper.getTranslationKey("filter", "tooltip")));
            filter.forEach((slot, filterHandler) -> {
                if(!filterHandler.isEmpty()) {
                    tooltip.add(String.format("   [%s/%s] %s",
                            (filterHandler.getMatchMeta() ? TextFormatting.YELLOW : TextFormatting.DARK_GRAY) + "Meta" + TextFormatting.GRAY,
                            (filterHandler.getMatchOre() ? TextFormatting.YELLOW : TextFormatting.DARK_GRAY) + "Ore" + TextFormatting.GRAY,
                            TextFormatting.WHITE + filterHandler.getStack().getDisplayName()
                    ));
                }
            });
        }
    }

    @Override
    protected void addCtrlTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getTranslationKey("enableinfo", "tooltip")));
    }
}
