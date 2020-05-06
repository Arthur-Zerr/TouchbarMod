package de.pkcstudio.touchbarmod.TouchbarWrapper;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWNativeCocoa;

import com.thizzer.jtouchbar.JTouchBar;
import com.thizzer.jtouchbar.common.Color;
import com.thizzer.jtouchbar.common.Image;
import com.thizzer.jtouchbar.item.TouchBarItem;
import com.thizzer.jtouchbar.item.view.TouchBarButton;
import com.thizzer.jtouchbar.item.view.action.TouchBarViewAction;

import net.minecraft.client.Minecraft;

public class TouchbarWrapper {

    public JTouchBar touchbar = new JTouchBar();
    public List<CustomTouchBarItem> customItems = new ArrayList<CustomTouchBarItem>();
	
	public static TouchbarWrapper touchbarWrapper;
	/**
	 * The singleton of the TouchbarWrapper
	 * @return the singleton of the TouchbarWrapper
	 */
	public static TouchbarWrapper getInstance(){
		if(touchbarWrapper == null){
			touchbarWrapper = new TouchbarWrapper();
		}
		return touchbarWrapper;
	}

    public TouchbarWrapper() {
		Reload();
	}
	
	/**
	* Reloads the Touchbar
	*/
    public void Reload() 
    {
    	long window = Minecraft.getInstance().func_228018_at_().getHandle();
    	touchbar.hide(
    			GLFWNativeCocoa.glfwGetCocoaWindow(window)
    		);
    	
    	touchbar.show(
        		GLFWNativeCocoa.glfwGetCocoaWindow(window)
        	);
    }
	
	/**
	 * Updates or Creates the Touchbar Item with the corresponding key and other information
	 * @param action The TouchbarButton action
	 * @param text The text shown on the TouchbarButton
	 * @param showText  or not to shown text on the TouchbarButton
	 * @param img The image shown on the TouchbarButton
	 */
    public void UpdateTouchbarItem(TouchBarViewAction action, String key, String text, byte[] img) {
    	CustomTouchBarItem tempItem = FindItemByKey(key);
    	
    	if(tempItem != null) {
            customItems.remove(tempItem);
    		tempItem.item = null;
            tempItem.item = createTouchBarButton(action, text, img, false);
            customItems.add(Integer.parseInt(key.substring(5)), tempItem);
    	} else {
            customItems.add(new CustomTouchBarItem(key, createTouchBarButton(action, text, img, false)));
    	}
	}
	
    /**
	 * Updates the Touchbar with the Changes made
	 */
    public void FinishedUpdate()
    {
        UpdateTouchbar();
    }
    
    private void UpdateTouchbar()
    {
    	List<TouchBarItem> tempList = new ArrayList<TouchBarItem>();
    	Reload();
    	for(CustomTouchBarItem item : customItems)
    	{
    		tempList.add(new TouchBarItem(item.key, item.item, true));
    	}

    	touchbar.setItems(tempList);
	}
	
	/**
	 * Creates a new TouchbarButton with already set Settings
     * @return A new Custom TouchbarButton
	 * @param action The TouchbarButton action
	 * @param text The text shown on the TouchbarButton
	 * @param showText  or not to shown text on the TouchbarButton
	 * @param img The image shown on the TouchbarButton
    */
	private TouchBarButton createTouchBarButton(TouchBarViewAction action, String text, byte[] img, boolean showText){
		TouchBarButton tempButton = new TouchBarButton();
		tempButton.setBezelColor(Color.LIGHT_GRAY);

		if(action != null){
			tempButton.setAction(action);
		}
		if(img != null)
		{
			Image image = new Image(img);
			tempButton.setImage(image);
		}
		if(showText == true){
			tempButton.setTitle(text);
		}

		return tempButton;
	}

	/**
	 * Searches the list for the corresponding CustomTouchBarItem from the list
     * @return The item with the corresponding key or null when the item is not found
	 * @param key Key of the item
    */
    private CustomTouchBarItem FindItemByKey(String key)
    {
    	if(customItems == null)
    	{
    		throw new NullPointerException("CustomTouchbarItems list is null");
    	}
    	
    	for(CustomTouchBarItem item : customItems)
    	{
    		if(item.key.equals(key))
    			return item;
    	}
    	return null;
    }  
}
