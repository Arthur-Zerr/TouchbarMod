package de.pkcstudio.touchbarmod.TouchbarWrapper;

import com.thizzer.jtouchbar.item.view.TouchBarView;

public class CustomTouchBarItem 
{
	public String key;
	
	public TouchBarView item;
	
	public CustomTouchBarItem(String key, TouchBarView item)
	{
		this.key = key;
		this.item = item;
	}
}
