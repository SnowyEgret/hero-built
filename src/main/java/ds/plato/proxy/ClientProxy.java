package ds.plato.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ds.plato.event.ForgeEventHandler;
import ds.plato.event.KeyHandler;
import ds.plato.event.MouseHandler;
import ds.plato.gui.Overlay;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		MinecraftForge.EVENT_BUS.register(new MouseHandler());
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}
}
