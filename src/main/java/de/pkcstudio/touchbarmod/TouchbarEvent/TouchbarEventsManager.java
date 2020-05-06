package de.pkcstudio.touchbarmod.TouchbarEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class TouchbarEventsManager {

	private static final Logger LOGGER = LogManager.getLogger();

	private String[] itemInHotbar;

	@SubscribeEvent
	public void onWorldLoad(Load event) {
		Reset();
	}

	@SubscribeEvent
	public void onWorldUnload(Unload event){
		Reset();
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.CLIENT) {
			for (int i = 0; i < 8; i++) {
				ItemStack itemstack = event.player.inventory.getStackInSlot(i);
				String itemName = itemstack.getDisplayName().getString();

				if (itemInHotbar[i].equals(itemName) == false) {
					onInventoryChangedEvent(new InventoryChangedEvent(i, itemstack));
				}
			}
		}
	}

	/**
	 * Raised when the Inventory Hotbar changed
	 * @param inventoryChangedEvent Contains some information about the event
	 */
	public void onInventoryChangedEvent(InventoryChangedEvent inventoryChangedEvent)
	{

	}

	/**
	 * Reset the items in the list
	 */
	private void Reset(){
		LOGGER.info("Hotbar Items Cleared");
		itemInHotbar = new String[10];
		for (int i = 0; i < 9; i++) {
			itemInHotbar[i] = "";
		}
	}
}
