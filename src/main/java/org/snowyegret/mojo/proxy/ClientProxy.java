package org.snowyegret.mojo.proxy;

import org.snowyegret.mojo.event.EventHandler;
import org.snowyegret.mojo.event.KeyHandler;
import org.snowyegret.mojo.event.MouseHandler;
import org.snowyegret.mojo.gui.Overlay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new MouseHandler());
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}
}
