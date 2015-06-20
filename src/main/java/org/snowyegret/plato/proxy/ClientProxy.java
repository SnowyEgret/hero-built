package org.snowyegret.plato.proxy;

import org.snowyegret.plato.event.EventHandler;
import org.snowyegret.plato.event.KeyHandler;
import org.snowyegret.plato.event.MouseHandler;
import org.snowyegret.plato.gui.Overlay;

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
