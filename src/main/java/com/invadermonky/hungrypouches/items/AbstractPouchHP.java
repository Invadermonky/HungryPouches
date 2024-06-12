package com.invadermonky.hungrypouches.items;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.handlers.ConfigHandlerHP;
import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.handlers.StackHandlerPouch;
import com.invadermonky.hungrypouches.init.EnchantmentRegistryHP;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchSkeletal;
import com.invadermonky.hungrypouches.util.NBTHelper;
import com.invadermonky.hungrypouches.util.RayTraceHelper;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import com.invadermonky.hungrypouches.util.StringHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public abstract class AbstractPouchHP extends ItemHP {
    public AbstractPouchHP(String unlocName) {
        super(unlocName);
        setMaxStackSize(1);
        addPropertyOverride(new ResourceLocation(HungryPouches.MOD_ID, "enabled"), (itemStack, world, entityLivingBase) -> PouchHandler.isEnabled(itemStack) ? 1 : 0);
    }

    public int getMaxSlots(ItemStack pouch) {
        int gluttonyLevel = EnchantmentRegistryHP.enableGluttonousEnchant ? EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistryHP.gluttonous, pouch) : ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.GLUTTONOUS.pouchFallbackSize;
        switch (gluttonyLevel) {
            case 0: return 9;
            case 1: return 12;
            case 2: return 15;
            default: return 18;
        }
    }

    public int getGuiId() {
        return ReferencesHP.GUI_ID_POUCH;
    };

    @Override
    public boolean onEntitySwing(@Nonnull EntityLivingBase entityLiving, @Nonnull ItemStack stack) {
        if(entityLiving instanceof EntityPlayer && stack.hasTagCompound() && stack.getTagCompound().hasKey(ReferencesHP.TAG_INVENTORY)) {
            RayTraceResult trace = RayTraceHelper.retrace((EntityPlayer) entityLiving);
            if(trace.typeOfHit == RayTraceResult.Type.MISS) {
                PouchHandler.shuffleContents((EntityPlayer) entityLiving, stack);
            }
        }
        return false;
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 12;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(player instanceof FakePlayer)
            return new ActionResult<>(EnumActionResult.FAIL, stack);

        NBTHelper.initNBT(stack);
        RayTraceResult trace = RayTraceHelper.retrace(player);
        if(player.isSneaking()) {
            TileEntity tile = player.world.getTileEntity(trace.getBlockPos());
            if(!(tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, trace.sideHit))) {
                if (PouchHandler.isEnabled(stack)) {
                    PouchHandler.setEnabled(stack, false);
                    player.sendStatusMessage(new TextComponentTranslation("chat." + HungryPouches.MOD_ID + ":disabled"), true);
                } else {
                    PouchHandler.setEnabled(stack, true);
                    player.sendStatusMessage(new TextComponentTranslation("chat." + HungryPouches.MOD_ID + ":enabled"), true);
                }
            }
        } else {
            if (!player.isCreative() && ConfigHandlerHP.GENERAL_SETTINGS.enableBiting && PouchHandler.isEnabled(stack) && !player.world.getDifficulty().equals(EnumDifficulty.PEACEFUL) && !(stack.getItem() instanceof ItemPouchSkeletal)) {
                player.attackEntityFrom(DamageSource.GENERIC, ConfigHandlerHP.GENERAL_SETTINGS.biteDamage);
                player.sendStatusMessage(new TextComponentTranslation("chat." + HungryPouches.MOD_ID + ":biting"), true);
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1.0f, 1.5f);
            }
            PouchHandler.openPouchGUI(player, this.getGuiId());
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if(world.isAirBlock(pos) || world.isRemote)
            return EnumActionResult.PASS;

        PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
        if(MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
            return EnumActionResult.PASS;

        if(player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);
            if(tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
                ItemStack stack = player.getHeldItem(hand);
                IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
                PouchHandler.emptyIntoTarget(stack, capability);
            }
        }
        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        addDefaultTooltip(stack, tooltip);
        if(GuiScreen.isShiftKeyDown()) {
            addShiftTooltip(stack, tooltip);
        } else if(GuiScreen.isCtrlKeyDown()) {
            addCtrlTooltip(stack, tooltip);
        } else {
            addNoKeyTooltip(stack, tooltip);
        }
    }

    /**
     * Adds default, always shown tooltip to an item.
     * @param stack The stack to add the tooltip to.
     * @param tooltip The List of tooltip strings.
     */
    protected void addDefaultTooltip(ItemStack stack, List<String> tooltip) {
        if(PouchHandler.isEnabled(stack))
            tooltip.add(I18n.format(StringHelper.getLanguageKey("enabled", "tooltip")));
        else
            tooltip.add(I18n.format(StringHelper.getLanguageKey("disabled", "tooltip")));
        tooltip.add(I18n.format(StringHelper.getLanguageKey("slotinfo", "tooltip"), PouchHandler.getUsedSlots(stack), PouchHandler.getMaxSlots(stack)));
    }

    /**
     * Adds shift-key down tooltips to an item.
     * @param stack The stack to add the tooltip to.
     * @param tooltip The List of tooltip strings.
     */
    protected void addNoKeyTooltip(ItemStack stack, List<String> tooltip) {
        if(ConfigHandlerHP.GENERAL_SETTINGS.enableUsageTooltips) {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("info", "tooltip")));
            tooltip.add(I18n.format(StringHelper.getLanguageKey("usage", "tooltip")));
        }
    }

    /**
     * Adds shift-key down tooltips to an item.
     * @param stack The stack to add the tooltip to.
     * @param tooltip The List of tooltip strings.
     */
    protected void addShiftTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getLanguageKey(Objects.requireNonNull(getRegistryName()).getPath() + ".desc", "tooltip")));

        TreeMap<Integer, StackHandlerPouch> contents = PouchHandler.getPouchContents(stack);
        if(contents.isEmpty()) {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("empty", "tooltip")));
        } else {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("contents", "tooltip")));
            for (StackHandlerPouch stackHandler : contents.values()) {
                tooltip.add("   " + TextFormatting.GOLD + stackHandler.getCount() + TextFormatting.RESET + " " + stackHandler.getStack().getDisplayName());
            }
        }
    }

    /**
     * Adds tooltips that will display when neither shift nor ctrl are pressed.
     * @param stack The stack to add the tooltip to.
     * @param tooltip The List of tooltip strings.
     */
    protected void addCtrlTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getLanguageKey("enableinfo", "tooltip")));
        tooltip.add(I18n.format(StringHelper.getLanguageKey("transferinfo", "tooltip")));
        tooltip.add(I18n.format(StringHelper.getLanguageKey("shuffleinfo", "tooltip")));
    }

    /**
     * Checks to see if the {@link EntityItemPickupEvent} will be handled by the passed Hungry Pouch, and places the
     * event item into the Hungry Pouch if it is valid.
     *
     * @param event The item pickup event
     * @param pouch The Hungry Pouch stack being queried
     * @return true if all items in the event are eaten by the hungry pouch, false if there are items remaining
     */
    public boolean onItemPickup(EntityItemPickupEvent event, ItemStack pouch) {
        return PouchHandler.onItemPickup(event, pouch, true);
    }
}
