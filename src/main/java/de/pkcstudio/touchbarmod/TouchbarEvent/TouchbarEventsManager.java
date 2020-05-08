package de.pkcstudio.touchbarmod.TouchbarEvent;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class TouchbarEventsManager {

	private String[] itemInHotbar;
	private List<ITouchbarEvent> listeners = new ArrayList<ITouchbarEvent>();
	
	private static TouchbarEventsManager touchbarEventsManager;
	/**
	 * The singleton of the TouchbarEventsManager
	 * @return the singleton of the TouchbarEventsManager
	 */
	public static TouchbarEventsManager getInstance(){
		if(touchbarEventsManager == null){
			touchbarEventsManager = new TouchbarEventsManager();
		}
		return touchbarEventsManager;
	}


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
					itemInHotbar[i] = itemName;
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
		for (ITouchbarEvent event : listeners) {
			event.onInventoryChangedEvent(inventoryChangedEvent);
		}
	}

	/**
	 * Add a Listener to the event list
	 * @param listener 
	 */
	public void addListener(ITouchbarEvent listener)
	{
		listeners.add(listener);
	}
	/**
	 * Reset the items in the list
	 */
	private void Reset(){
		itemInHotbar = new String[10];
		for (int i = 0; i < 9; i++) {
			itemInHotbar[i] = "";
		}
	}
}
