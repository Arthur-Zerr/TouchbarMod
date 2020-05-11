package de.pkcstudio.touchbarmod.TouchbarWrapper;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWNativeCocoa;

import de.pkcstudio.touchbarmod.TouchbarHelper.ImageConvert;

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
	
	private final boolean SHOW_TEXT = false;

    private static final Logger LOGGER = LogManager.getLogger();
	
	private static TouchbarWrapper touchbarWrapper;
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
    public void Reload() {
    	long window = Minecraft.getInstance().func_228018_at_().getHandle();
    	touchbar.hide(
    			GLFWNativeCocoa.glfwGetCocoaWindow(window)
    		);
    	
    	touchbar.show(
        		GLFWNativeCocoa.glfwGetCocoaWindow(window)
        	);
	}
	
	public void ShowMinecraftLogo(){
		TouchBarButton minecraftLogo = new TouchBarButton();
		minecraftLogo.setTitle("Minecraft");
		try {
			File logoPath = new File("resources/Minecraft-Logo.png");
			BufferedImage image = ImageIO.read(logoPath);
			minecraftLogo.setImage(new Image(ImageConvert.getImgBytes(image)));
		} catch (Exception e) {
			LOGGER.error(e);	
		}

		touchbar.addItem(new TouchBarItem("minecraftLogo", minecraftLogo));
	}
	
	/**
	 * Updates or Creates the Touchbar Item with the corresponding key and other information
	 * @param action The TouchbarButton action
	 * @param text The text shown on the TouchbarButton
	 * @param showText  or not to shown text on the TouchbarButton
	 * @param img The image shown on the TouchbarButton
	 */
    public void UpSertTouchbarItem(TouchBarViewAction action, String key, String text, byte[] img) {
    	CustomTouchBarItem tempItem = FindItemByKey(key);
    	
    	if(tempItem != null) {
            customItems.remove(tempItem);
    		tempItem.item = null;
            tempItem.item = createTouchBarButton(action, text, img, SHOW_TEXT);
            customItems.add(Integer.parseInt(key.substring(5)), tempItem);
    	} else {
            customItems.add(new CustomTouchBarItem(key, createTouchBarButton(action, text, img, SHOW_TEXT)));
    	}
	}
	
    /**
	 * Updates the Touchbar with the Changes made
	 */
    public void FinishedUpdate() {
        UpdateTouchbar();
    }
    
    private void UpdateTouchbar() {
    	List<TouchBarItem> tempList = new ArrayList<TouchBarItem>();
    	Reload();
    	for(CustomTouchBarItem item : customItems) {
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
		if(img != null) {
			Image image = new Image(img);
			tempButton.setImage(image);
		}
		if(showText == true){
			if(text.equals("Air")){
				text = "";
			}
			tempButton.setTitle(text);
		}

		return tempButton;
	}

	/**
	 * Searches the list for the corresponding CustomTouchBarItem from the list
     * @return The item with the corresponding key or null when the item is not found
	 * @param key Key of the item
    */
    private CustomTouchBarItem FindItemByKey(String key) {
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
