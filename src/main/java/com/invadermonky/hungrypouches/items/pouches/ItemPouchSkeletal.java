package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.handlers.ConfigHandler;
import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.handlers.PouchSlotHandler;
import com.invadermonky.hungrypouches.init.ItemRegistryHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.util.RayTraceHelper;
import com.invadermonky.hungrypouches.util.StringHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

public class ItemPouchSkeletal extends AbstractPouchHP {
    public ItemPouchSkeletal(String unlocName) {
        super(unlocName);
    }

    public static List<Item> getEnabledPouches() {
        List<Item> pouchList = new ArrayList<>();
        if(ConfigHandler.CROP_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.cropPouch);
        }
        if(ConfigHandler.MOB_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.mobPouch);
        }
        if(ConfigHandler.ORE_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.orePouch);
        }
        if(ConfigHandler.VOID_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.voidPouch);
        }
        return pouchList;
    }

    @Override
    public int getMaxSlots(ItemStack pouch) {
        return getEnabledPouches().size() + 1;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if(entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            RayTraceResult trace = RayTraceHelper.retrace(player);
            if(trace.typeOfHit == RayTraceResult.Type.MISS) {
                TreeMap<Integer,PouchSlotHandler> contents = PouchHandler.getPouchContents(stack);
                TreeMap<Integer,PouchSlotHandler> newContents = new TreeMap<>();

                for(Entry<Integer,PouchSlotHandler> entry : contents.entrySet()) {
                    ItemStack slotStack = entry.getValue().getStack();
                    if(!slotStack.isEmpty()) {
                        PouchHandler.shuffleContents(player, slotStack);
                        newContents.put(entry.getKey(), new PouchSlotHandler(slotStack));
                    }
                }
                PouchHandler.setPouchContents(stack, newContents);
            }
        }
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
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
                for(PouchSlotHandler slotHandler : PouchHandler.getPouchContents(stack).values()) {
                    if(!slotHandler.getStack().isEmpty()) {
                        PouchHandler.emptyIntoTarget(slotHandler.getStack(), capability);
                    }
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    protected void addDefaultTooltip(ItemStack stack, List<String> tooltip) {
        if(PouchHandler.isEnabled(stack))
            tooltip.add(I18n.format(StringHelper.getLanguageKey("feeding", "tooltip")));
        else
            tooltip.add(I18n.format(StringHelper.getLanguageKey("disabled", "tooltip")));
    }

    @Override
    protected void addNoKeyTooltip(ItemStack stack, List<String> tooltip) {}

    @Override
    protected void addShiftTooltip(ItemStack stack, List<String> tooltip) {
        tooltip.add(I18n.format(StringHelper.getLanguageKey(Objects.requireNonNull(getRegistryName()).getPath() + ".desc", "tooltip")));

        TreeMap<Integer, PouchSlotHandler> contents = PouchHandler.getPouchContents(stack);
        if(contents.isEmpty()) {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("empty", "tooltip")));
        } else {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("contents", "tooltip")));
            for(PouchSlotHandler slotHandler : contents.values()) {
                ItemStack stackSlot = slotHandler.getStack();
                String enabled = PouchHandler.isEnabled(stackSlot) ? I18n.format(StringHelper.getLanguageKey("enabled", "tooltip")) : I18n.format(StringHelper.getLanguageKey("disabled", "tooltip"));
                tooltip.add("   " + TextFormatting.RESET + stackSlot.getDisplayName() + " - " + enabled);
            }
        }
    }

    @Override
    public boolean onItemPickup(EntityItemPickupEvent event, ItemStack pouch) {
        if(!PouchHandler.isEnabled(pouch))
            return false;

        ItemStack eventStack = event.getItem().getItem();
        int oldCount = eventStack.getCount();

        TreeMap<Integer,PouchSlotHandler> contents = PouchHandler.getPouchContents(pouch);
        TreeMap<Integer,PouchSlotHandler> newContents = new TreeMap<>();
        newContents.putAll(contents);

        for(Entry<Integer, PouchSlotHandler> entry : contents.entrySet()) {
            ItemStack pouchStack = entry.getValue().getStack();
            if(!pouchStack.isEmpty() && PouchHandler.onItemPickup(event, pouchStack, false)) {
                newContents.put(entry.getKey(), new PouchSlotHandler(pouchStack));
            }
            if(eventStack.isEmpty())
                break;
        }

        if(eventStack.getCount() != oldCount) {
            pouch.setAnimationsToGo(5);
            PouchHandler.playPickupSound(event.getEntityPlayer(), PouchHandler.getPickupSound(pouch));
            PouchHandler.setPouchContents(pouch, newContents);
        }
        return eventStack.isEmpty();
    }
}
