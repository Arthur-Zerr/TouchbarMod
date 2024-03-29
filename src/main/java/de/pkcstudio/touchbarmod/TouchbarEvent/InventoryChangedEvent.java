package de.pkcstudio.touchbarmod.TouchbarEvent;

import net.minecraft.item.ItemStack;

public class InventoryChangedEvent {
    
    private int Slot;
    private ItemStack ItemStack;
    
    public InventoryChangedEvent(int slot, ItemStack itemstack){
        this.Slot = slot;
        this.ItemStack = itemstack;
    }

    /**
     * @return the slot
     */
    public int getSlot() {
        return Slot;
    }

    /**
     * @return the itemStack
     */
    public ItemStack getItemStack() {
        return ItemStack;
    }
}