package de.pkcstudio.touchbarmod;

import de.pkcstudio.touchbarmod.TouchbarEvent.ITouchbarEvent;
import de.pkcstudio.touchbarmod.TouchbarEvent.InventoryChangedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TouchbarLogic implements ITouchbarEvent {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInventoryChangedEvent(InventoryChangedEvent inventoryChangedEvent) {
        LOGGER.info("Hotbar Changed Slot: " + inventoryChangedEvent.getSlot() + ", Item: " + inventoryChangedEvent.getItemStack().getDisplayName().getString());
    }
    
}