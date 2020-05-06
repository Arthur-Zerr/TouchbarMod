package de.pkcstudio.touchbarmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.pkcstudio.touchbarmod.TouchbarEvent.TouchbarEventsManager;
import de.pkcstudio.touchbarmod.TouchbarWrapper.TouchbarWrapper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("touchbar")
public class TouchbarMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private TouchbarWrapper touchbarWrapper;

    public TouchbarMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TouchbarEventsManager());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        touchbarWrapper = TouchbarWrapper.getInstance();
    }

    private void doClientStuff(final FMLClientSetupEvent event) 
    {
    }
}
