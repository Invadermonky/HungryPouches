package com.invadermonky.hungrypouches.items.pouches;

import com.invadermonky.hungrypouches.handlers.ConfigHandlerHP;
import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.handlers.StackHandlerPouch;
import com.invadermonky.hungrypouches.init.ItemRegistryHP;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.util.RayTraceHelper;
import com.invadermonky.hungrypouches.util.ReferencesHP;
import com.invadermonky.hungrypouches.util.StringHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
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

    public static List<Item> getRegisteredPouches() {
        List<Item> pouchList = new ArrayList<>();
        if(ConfigHandlerHP.CROP_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.cropPouch);
        }
        if(ConfigHandlerHP.MOB_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.mobPouch);
        }
        if(ConfigHandlerHP.ORE_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.orePouch);
        }
        if(ConfigHandlerHP.VOID_POUCH.enablePouch) {
            pouchList.add(ItemRegistryHP.voidPouch);
        }
        return pouchList;
    }

    @Override
    public int getGuiId() {
        return ReferencesHP.GUI_ID_SKELETAL;
    }

    @Override
    public int getMaxSlots(ItemStack pouch) {
        return getRegisteredPouches().size() + 1;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if(entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            RayTraceResult trace = RayTraceHelper.retrace(player);
            if(trace.typeOfHit == RayTraceResult.Type.MISS) {
                TreeMap<Integer, StackHandlerPouch> contents = PouchHandler.getPouchContents(stack);

                for(Entry<Integer, StackHandlerPouch> entry : contents.entrySet()) {
                    ItemStack slotStack = entry.getValue().getStack();
                    if(!slotStack.isEmpty()) {
                        PouchHandler.shuffleContents(player, slotStack);
                        contents.put(entry.getKey(), entry.getValue().setStack(slotStack));
                    }
                }
                PouchHandler.setPouchContents(stack, contents);
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
                ItemStack skeletalStack = player.getHeldItem(hand);
                IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);

                TreeMap<Integer, StackHandlerPouch> contents = PouchHandler.getPouchContents(skeletalStack);

                for(Entry<Integer, StackHandlerPouch> entry : contents.entrySet()) {
                    if(!entry.getValue().getStack().isEmpty()) {
                        ItemStack pouchStack = entry.getValue().getStack();
                        PouchHandler.emptyIntoTarget(pouchStack, capability);
                        contents.put(entry.getKey(), entry.getValue().setStack(pouchStack));
                    }
                }
                PouchHandler.setPouchContents(skeletalStack, contents);
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

        TreeMap<Integer, StackHandlerPouch> contents = PouchHandler.getPouchContents(stack);
        if(contents.isEmpty()) {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("empty", "tooltip")));
        } else {
            tooltip.add(I18n.format(StringHelper.getLanguageKey("contents", "tooltip")));
            for(StackHandlerPouch slotHandler : contents.values()) {
                ItemStack stackSlot = slotHandler.getStack();
                String enabled = PouchHandler.isEnabled(stackSlot) ? I18n.format(StringHelper.getLanguageKey("enabled", "tooltip")) : I18n.format(StringHelper.getLanguageKey("disabled", "tooltip"));
                tooltip.add("   " + TextFormatting.RESET + stackSlot.getDisplayName() + " - " + enabled);
            }
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        /* TODO: Get pouch animation to run in containers. This may be a difficult task as vanilla inventories don't animate stacks.
        //Ticking nested pouches
        if(entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).openContainer instanceof ContainerSkeletalPouch) {
            EntityPlayer player = (EntityPlayer) entityIn;
            ContainerSkeletalPouch container = (ContainerSkeletalPouch) player.openContainer;

            for(Slot slot : container.inventorySlots) {
                ItemStack slotStack = slot.getStack();
                if(slotStack.getAnimationsToGo() > 0)
                    slotStack.updateAnimation(worldIn, player, slot.slotNumber, ItemStack.areItemStacksEqual(slotStack, player.activeItemStack));
            }
            container.detectAndSendChanges();
        }
        */
    }

    @Override
    public boolean onItemPickup(EntityItemPickupEvent event, ItemStack pouch) {
        if(!PouchHandler.isEnabled(pouch))
            return false;

        ItemStack eventStack = event.getItem().getItem();
        int oldCount = eventStack.getCount();

        TreeMap<Integer, StackHandlerPouch> contents = PouchHandler.getPouchContents(pouch);

        for(Entry<Integer, StackHandlerPouch> entry : contents.entrySet()) {
            ItemStack pouchStack = entry.getValue().getStack();
            if(!pouchStack.isEmpty() && PouchHandler.onItemPickup(event, pouchStack, false)) {
                contents.put(entry.getKey(), entry.getValue().setStack(pouchStack));
            }
            if(eventStack.isEmpty())
                break;
        }

        if(eventStack.getCount() != oldCount) {
            pouch.setAnimationsToGo(5);
            PouchHandler.playPickupSound(event.getEntityPlayer(), pouch);
            PouchHandler.setPouchContents(pouch, contents);
        }
        return eventStack.isEmpty();
    }
}
