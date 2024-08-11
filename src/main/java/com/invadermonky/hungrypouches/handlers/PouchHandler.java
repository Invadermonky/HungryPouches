package com.invadermonky.hungrypouches.handlers;

import com.invadermonky.hungrypouches.HungryPouches;
import com.invadermonky.hungrypouches.init.ModEnchantsHP;
import com.invadermonky.hungrypouches.init.ModSoundsHP;
import com.invadermonky.hungrypouches.inventory.slots.SlotHungry;
import com.invadermonky.hungrypouches.items.AbstractPouchHP;
import com.invadermonky.hungrypouches.items.IHungryPouch;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchSkeletal;
import com.invadermonky.hungrypouches.items.pouches.ItemPouchVoid;
import com.invadermonky.hungrypouches.util.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class PouchHandler {
    /**
     * Determines whether the passed Hungry Pouch is enabled and will start eating item pickups.
     *
     * @param pouch The Hungry Pouch stack being queried
     * @return true if the Hungry Pouch is enabled
     */
    public static boolean isEnabled(ItemStack pouch) {
        return NBTHelper.getBoolean(pouch, ReferencesHP.TAG_ENABLED, false);
    }

    public static void setEnabled(ItemStack pouch, boolean enabled) {
        NBTHelper.setBoolean(pouch, ReferencesHP.TAG_ENABLED, enabled);
    }

    /**
     * Gets the number of used slots in the pouch inventory.
     *
     * @param pouch The Hungry Pouch stack being queried
     * @return The number of slots currently being used in the Hungry Pouch
     */
    public static int getUsedSlots(ItemStack pouch) {
        return NBTHelper.getTagCompound(pouch, ReferencesHP.TAG_INVENTORY).getKeySet().size();
    }

    /**
     * Gets the maximum number of available slots for the passed Hungry Pouch ItemStack.
     *
     * @param pouch The Hungry Pouch stack being queried
     * @return Maximum number of inventory slots.
     */
    public static int getMaxSlots(ItemStack pouch) {
        if(pouch.getItem() instanceof AbstractPouchHP) {
            return ((AbstractPouchHP) pouch.getItem()).getMaxSlots(pouch);
        } else {
            return 0;
        }
    }

    /**
     * Gets the item max stack size for the passed Hungry Pouch based on a stack size of 64
     *
     * @param pouch The Hungry Pouch stack being queried
     * @return Max stack size for the item in the hungry pouch
     */
    public static int getMaxStackSize(ItemStack pouch) {
        int size = 64;
        if(!ModEnchantsHP.enableInsatiableEnchant)
            return size;
        int enchLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantsHP.INSATIABLE, pouch);
        return ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.INSATIABLE.exponentialInsatiable ? size * (int) Math.pow(2, enchLevel) : size * (1 + enchLevel);
    }

    /**
     * Gets the item max stack size for the passed Hungry Pouch.
     *
     * @param pouch The Hungry Pouch stack being queried
     * @param item The item determining the maximum stack size
     * @return Max stack size for the item in the hungry pouch
     */
    public static int getMaxStackSize(ItemStack pouch, ItemStack item) {
        int size = item.getMaxStackSize();
        if(size == 1 || !ModEnchantsHP.enableInsatiableEnchant)
            return size;
        int enchLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantsHP.INSATIABLE, pouch);
        return ConfigHandlerHP.HUNGRY_POUCH_ENCHANTS.INSATIABLE.exponentialInsatiable ? size * (int) Math.pow(2, enchLevel) : size * (1 + enchLevel);
    }

    /**
     * Gets the item max stack size for the passed Hungry Pouch.
     *
     * @param pouch The Hungry Pouch stack being queried
     * @param item The item determining the maximum stack size
     * @param slot The slot the item is being placed into.
     * @return Max stack size for the item in the hungry pouch
     */
    public static int getMaxStackSize(ItemStack pouch, ItemStack item, Slot slot) {
        return (slot instanceof SlotHungry) ? getMaxStackSize(pouch, item) : Math.min(item.getMaxStackSize(), slot.getSlotStackLimit());
    }

    /**
     * Checks to see if the {@link EntityItemPickupEvent} will be handled by the passed Hungry Pouch, and places the
     * event item into the Hungry Pouch if it is valid.
     *
     * @param event The item pickup event
     * @param pouch The Hungry Pouch stack being queried
     * @param isInventoryItem true if the pouch is an item in the player's inventory. This will cause animations and sounds to play.
     * @return true if all items in the event are eaten by the hungry pouch, false if there are items remaining
     */
    public static boolean onItemPickup(EntityItemPickupEvent event, ItemStack pouch, boolean isInventoryItem) {
        return insertStackIntoPouch(event.getEntityPlayer(), pouch, event.getItem().getItem(), isInventoryItem, false);
    }

    /**
     * Attempts to insert the passed itemstack into the pouch.
     *
     * @param player The player holding the pouch.
     * @param pouch The Hungry Pouch stack being queried
     * @param stack The {@link ItemStack} being inserted.
     * @param isInventoryItem true if the pouch is an item in the player's inventory. This will cause animations and sounds to play.
     * @param forceFeed Forces the ItemStack into the bag, ignored enabled/disabled status.
     * @return true if all items in the event are eaten by the hungry pouch, false if there are items remaining
     */
    public static boolean insertStackIntoPouch(EntityPlayer player, ItemStack pouch, ItemStack stack, boolean isInventoryItem, boolean forceFeed) {
        if(!PouchHandler.isEnabled(pouch) && !forceFeed) {
            return false;
        }

        if(isWhitelistedItem(pouch, stack)) {
            if(pouch.getItem() instanceof ItemPouchVoid) {
                Random rand = new Random();
                stack.setCount(0);
                pouch.setAnimationsToGo(5);
                playPickupSound(player, pouch);
                //Tag modification is used to force the pouch to update animationsToGo
                NBTHelper.setInt(pouch, ReferencesHP.TAG_RAND, rand.nextInt());
                return true;
            }

            int oldCount = stack.getCount();
            TreeMap<Integer, StackHandlerPouch> pouchContents = getPouchContents(pouch);

            for(int i = 0; i < getMaxSlots(pouch); i++) {
                if(!pouchContents.containsKey(i)) {
                    pouchContents.put(i, new StackHandlerPouch(stack));
                    stack.setCount(0);
                } else if(ItemStack.areItemsEqual(pouchContents.get(i).getStack(), stack) && ItemStack.areItemStackTagsEqual(pouchContents.get(i).getStack(), stack)) {
                    StackHandlerPouch slotHandler = pouchContents.get(i);
                    int maxStackSize = getMaxStackSize(pouch, stack);

                    if((stack.getCount() + slotHandler.getCount()) <= maxStackSize) {
                        slotHandler.grow(stack.getCount());
                        stack.setCount(0);
                    } else {
                        int remainder = Math.max(0, (slotHandler.getCount() + stack.getCount()) - maxStackSize);
                        slotHandler.setCount(maxStackSize);
                        stack.setCount(remainder);
                    }
                }

                if(stack.isEmpty())
                    break;
            }

            if(stack.getCount() != oldCount) {
                if(isInventoryItem) {
                    pouch.setAnimationsToGo(5);
                    playPickupSound(player, pouch);
                }
                //TODO: Pouches not animating when picking up items in multiplayer.
                setPouchContents(pouch, pouchContents);
            }
            return stack.isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Gets the item pickup sound used whenever an item is eaten by a Hungry Pouch
     *
     * @param pouch The Hungry Pouch stack being queried
     * @return {@link SoundEvent} based on the configuration and pouch type
     */
    public static SoundEvent getPickupSound(ItemStack pouch) {
        if(HolidayHelper.isAprilFools()) {
            return ModSoundsHP.APRIL_1_PICKUP;
        } else if(pouch.getItem() instanceof ItemPouchSkeletal) {
            return ConfigHandlerHP.SKELETAL_POUCH.enableRattlePickup ? SoundEvents.ENTITY_SKELETON_HURT : SoundEvents.ENTITY_ITEM_PICKUP;
        } else if(pouch.getItem() instanceof ItemPouchVoid) {
            return ConfigHandlerHP.VOID_POUCH.enableSizzlePickup ? SoundEvents.BLOCK_LAVA_EXTINGUISH : SoundEvents.ENTITY_ITEM_PICKUP;
        } else if (pouch.getItem() instanceof IHungryPouch) {
            return ConfigHandlerHP.GENERAL_SETTINGS.enableChompPickup ? SoundEvents.ENTITY_GENERIC_EAT : SoundEvents.ENTITY_ITEM_PICKUP;
        }
        return SoundEvents.ENTITY_ITEM_PICKUP;
    }

    public static void playPickupSound(EntityPlayer player, ItemStack pouch) {
        SoundEvent sound = getPickupSound(pouch);
        Random rand = new Random();
        float volume = 0.3f;
        float pitch = (rand.nextFloat() - rand.nextFloat() * 0.7f + 1.0f);

        if(sound == ModSoundsHP.APRIL_1_PICKUP) {
            pitch = 1.0f;
        } else if(sound == SoundEvents.ENTITY_SKELETON_HURT) {
            volume = 0.075f;
            pitch -= 0.3f;
        } else if(sound == SoundEvents.BLOCK_LAVA_EXTINGUISH) {
            pitch -= 0.3f;
        }
        player.world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, volume, pitch);
    }

    /**
     * Checks to see if the passed item is a whitelisted item for this Hungry Pouch stack.
     *
     * @param pouch Pouch stack must be an instance of {@link IHungryPouch}
     * @param stack The ItemStack to check
     * @return true if stack is a whitelisted item
     */
    public static boolean isWhitelistedItem(ItemStack pouch, ItemStack stack) {
        try {
            if(pouch.getItem() instanceof ItemPouchVoid) {
                for(StackHandlerFilter filter : getFilterContents(pouch).values()) {
                    if(filter.matches(stack))
                        return true;
                }
            } else {
                IHungryPouch pouchItem = (IHungryPouch) pouch.getItem();
                //Item Whitelist
                if (pouchItem.getValidItems().contains(Objects.requireNonNull(stack.getItem().getRegistryName()).toString()) ||
                        pouchItem.getValidItems().contains(stack.getItem().getRegistryName().toString() + ":" + stack.getMetadata())) {
                    return true;
                }
                //Ore Whitelist
                for (int id : OreDictionary.getOreIDs(stack)) {
                    if (pouchItem.getValidOres().contains(id)) {
                        return true;
                    }
                }
            }
        } catch(Exception e) {
            LogHelper.error("Error occurred during configured item check.");
            LogHelper.error(e.getStackTrace());
        }
        return false;
    }

    /**
     * Returns a map of the pouch contents using the slot number and the slot handler that holds the item data.
     */
    public static TreeMap<Integer, StackHandlerPouch> getPouchContents(ItemStack pouch) {
        TreeMap<Integer, StackHandlerPouch> pouchContents = new TreeMap<>();
        NBTTagCompound inventoryCompound = NBTHelper.getTagCompound(pouch, ReferencesHP.TAG_INVENTORY);
        for(String slot : inventoryCompound.getKeySet()) {
            int slotIndex = Integer.parseInt(slot);
            NBTTagCompound slotCompound = inventoryCompound.getCompoundTag(slot);
            ItemStack slotStack = new ItemStack(slotCompound.getCompoundTag(ReferencesHP.TAG_ITEM));
            int stackCount = slotCompound.getInteger(ReferencesHP.TAG_COUNT);
            pouchContents.put(slotIndex, new StackHandlerPouch(slotStack, stackCount));
        }
        return pouchContents;
    }

    public static void setPouchContents(ItemStack pouch, TreeMap<Integer, StackHandlerPouch> slotHandlers) {
        NBTTagCompound newInventoryCompound = new NBTTagCompound();

        for(Map.Entry<Integer, StackHandlerPouch> slotEntry : slotHandlers.entrySet()) {
            String slotKey = String.valueOf(slotEntry.getKey());
            if(!slotEntry.getValue().getStack().isEmpty()) {
                newInventoryCompound.setTag(slotKey, slotEntry.getValue().getInventorySlotNBT());
            }
        }
        NBTTagCompound pouchCompound = pouch.hasTagCompound() ? pouch.getTagCompound() : new NBTTagCompound();
        pouchCompound.setTag(ReferencesHP.TAG_INVENTORY, newInventoryCompound);
    }

    public static TreeMap<Integer, StackHandlerFilter> getFilterContents(ItemStack pouch) {
        TreeMap<Integer, StackHandlerFilter> filterContents = new TreeMap<>();
        NBTTagCompound inventoryCompound = NBTHelper.getTagCompound(pouch, ReferencesHP.TAG_INVENTORY);
        for(String slot : inventoryCompound.getKeySet()) {
            int slotIndex = Integer.parseInt(slot);
            NBTTagCompound slotCompound = inventoryCompound.getCompoundTag(slot);
            ItemStack slotStack = new ItemStack(slotCompound.getCompoundTag(ReferencesHP.TAG_ITEM));
            boolean matchMeta = slotCompound.getBoolean(ReferencesHP.TAG_META);
            boolean matchOre = slotCompound.getBoolean(ReferencesHP.TAG_ORE);
            filterContents.put(slotIndex, new StackHandlerFilter(slotStack, matchMeta, matchOre));
        }
        return filterContents;
    }

    public static void setFilterContents(ItemStack pouch, TreeMap<Integer, StackHandlerFilter> filterHandlers) {
        NBTTagCompound newInventoryCompound = new NBTTagCompound();
        for(Map.Entry<Integer, StackHandlerFilter> filterEntry : filterHandlers.entrySet()) {
            String slotKey = String.valueOf(filterEntry.getKey());
            if(!filterEntry.getValue().getStack().isEmpty()) {
                newInventoryCompound.setTag(slotKey, filterEntry.getValue().getFilterSlotNBT());
            }
        }
        NBTTagCompound pouchCompound = pouch.hasTagCompound() ? pouch.getTagCompound() : new NBTTagCompound();
        pouchCompound.setTag(ReferencesHP.TAG_INVENTORY, newInventoryCompound);
    }

    /**
     * Shuffles the contents of the passed hungry pouch and combines stacks when possible. Will eject items
     * that do not fit in the pouch (this only occurs when enchants are removed or settings are changed).
     */
    public static void shuffleContents(EntityPlayer player, ItemStack pouch) {
        if(pouch.getItem() instanceof ItemPouchVoid)
            return;

        TreeMap<Integer, StackHandlerPouch> contents = getPouchContents(pouch);
        TreeMap<Integer, StackHandlerPouch> shuffledContents = new TreeMap<>();
        List<StackHandlerPouch> allItems = new ArrayList<>();

        int loopControl = 0;
        while(!contents.isEmpty() && loopControl < 20) {
            StackHandlerPouch entry =  contents.firstEntry().getValue();

            if(allItems.contains(entry)) {
                StackHandlerPouch item = allItems.get(allItems.indexOf(entry));
                item.grow(entry.getCount());
            } else {
                allItems.add(entry);
            }
            contents.remove(contents.firstKey());
            loopControl++;
        }

        int slot = 0;
        while(!allItems.isEmpty()) {
            if(slot < getMaxSlots(pouch)) {
                StackHandlerPouch slotHandler = allItems.get(0);
                int stackSize = getMaxStackSize(pouch, slotHandler.getStack());
                if(slotHandler.getCount() <= stackSize) {
                    shuffledContents.put(slot, slotHandler);
                    slot++;
                    allItems.remove(0);
                } else {
                    while(slotHandler.getCount() > 0 && slot < getMaxSlots(pouch)) {
                        int count = Math.min(slotHandler.getCount(), stackSize);
                        shuffledContents.put(slot, new StackHandlerPouch(slotHandler.getStack(), count));
                        slotHandler.shrink(count);
                        slot++;
                    }
                    if(slotHandler.getCount() <= 0) {
                        allItems.remove(0);
                    }
                }
            } else {
                vomitContents(player, allItems);
                break;
            }
        }
        setPouchContents(pouch, shuffledContents);
    }

    /**
     * Attempts to insert any items contained in the hungry pouch into the passed inventory target.
     */
    public static void emptyIntoTarget(ItemStack pouch, IItemHandler target) {
        if(target == null)
            return;

        TreeMap<Integer, StackHandlerPouch> contents = getPouchContents(pouch);
        for(StackHandlerPouch slotHandler : contents.values()) {
            ItemStack stackCopy = slotHandler.getStack();

            if(!stackCopy.isEmpty()) {
                for(int i = 0; i < target.getSlots(); i++) {
                    stackCopy = target.insertItem(i, stackCopy, false);
                    if(stackCopy.isEmpty()) {
                        break;
                    }
                }
            }
            slotHandler.setCount(stackCopy.getCount());
        }
        contents.keySet().removeIf((key) -> contents.get(key).getCount() == 0 || contents.get(key).getStack().isEmpty());
        setPouchContents(pouch, contents);
    }

    public static void vomitContents(EntityPlayer player, Collection<StackHandlerPouch> vomitItems) {
        Random rand = new Random();
        if(!vomitItems.isEmpty() && ConfigHandlerHP.GENERAL_SETTINGS.enableVomitSound) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ZOMBIE_DEATH, SoundCategory.PLAYERS, 0.4f, 0.7f);
        }

        for(StackHandlerPouch itemHandler : vomitItems) {
            int totalCount = itemHandler.getCount();
            int loopControl = 0;
            while (totalCount > 0 && loopControl < 10) {
                ItemStack stackCopy = itemHandler.getStack().copy();
                stackCopy.setCount(Math.min(totalCount, stackCopy.getMaxStackSize()));
                totalCount = totalCount - stackCopy.getCount();
                EntityItem stackEntity = new EntityItem(player.world, player.posX, player.posY + (player.height * 0.5), player.posZ, stackCopy);
                stackEntity.setPickupDelay(40);
                stackEntity.setVelocity((rand.nextDouble() - 0.5) * 0.25, (rand.nextDouble() - 0.20) * 0.25, (rand.nextDouble() - 0.5) * 0.25);
                if(!player.world.isRemote) {
                    player.world.spawnEntity(stackEntity);
                }
                loopControl++;
            }
        }
    }

    public static void openPouchGUI(EntityPlayer player, int guiId) {
        player.openGui(HungryPouches.INSTANCE, guiId, player.world, 0,0,0);
    }

    public static SpriteResourceLocationHP getPouchGuiTexture(ItemStack pouch) {
        if(pouch.getItem() instanceof ItemPouchVoid) {
            return ReferencesHP.GUI_POUCH_VOID;
        } else if(pouch.getItem() instanceof ItemPouchSkeletal) {
            return ReferencesHP.GUI_POUCH_SKELETAL;
        } else {
            switch (getMaxSlots(pouch)) {
                case 9:
                    return ReferencesHP.GUI_POUCH_SMALL;
                case 12:
                    return ReferencesHP.GUI_POUCH_MEDIUM;
                case 15:
                    return ReferencesHP.GUI_POUCH_LARGE;
                default:
                    return ReferencesHP.GUI_POUCH_HUGE;
            }
        }
    }
}
