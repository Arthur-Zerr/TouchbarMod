package de.pkcstudio.touchbarmod;

import de.pkcstudio.touchbarmod.TouchbarEvent.ITouchbarEvent;
import de.pkcstudio.touchbarmod.TouchbarEvent.InventoryChangedEvent;
import de.pkcstudio.touchbarmod.TouchbarHelper.ImageConvert;
import de.pkcstudio.touchbarmod.TouchbarWrapper.TouchbarWrapper;
import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TouchbarLogic implements ITouchbarEvent {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft INSTANCE = Minecraft.getInstance();
    private TouchbarWrapper touchbarWrapper;

    public TouchbarLogic() {
        touchbarWrapper = TouchbarWrapper.getInstance();
    }

    @Override
    public void onInventoryChangedEvent(InventoryChangedEvent inventoryChangedEvent) {
        INSTANCE.getProfiler().startSection("Touchbar Update");
        touchbarWrapper.UpSertTouchbarItem(null, "item_" + inventoryChangedEvent.getSlot(), inventoryChangedEvent.getItemStack().getDisplayName().getString(),ImageConvert.GetTexture(inventoryChangedEvent.getItemStack()));
        touchbarWrapper.FinishedUpdate();

        LOGGER.info("Hotbar Changed Slot: " + inventoryChangedEvent.getSlot() + ", Item: " + inventoryChangedEvent.getItemStack().getDisplayName().getString());
        INSTANCE.getProfiler().endSection();
    }
}