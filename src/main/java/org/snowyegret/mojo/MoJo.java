package org.snowyegret.mojo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.gui.PickInfo;
import org.snowyegret.mojo.gui.SelectionInfo;

@Mod(modid = MoJo.MODID, name = MoJo.NAME, version = MoJo.VERSION)
public class MoJo {

	public static final String MODID = "mojo";
	public static final String NAME = "MoJo";
	public static final String VERSION = "0.5";
	public static final String DOMAIN = "org.snowyegret.mojo";

	@Instance(MODID)
	public static MoJo instance;
	@SidedProxy(clientSide = DOMAIN + ".ClientProxy", serverSide = DOMAIN + ".CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	public static BlockSelected blockSelected;
	public static BlockPicked blockPicked;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.registerBlocks();
		proxy.registerItems();
		proxy.registerNetworkMessages();
		proxy.setCustomStateMappers();
		//NetworkRegistry.INSTANCE.registerGuiHandler(MoJo.instance, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerGuiHandler();
		proxy.registerEventHandlers();
		proxy.registerTileEntities();
		proxy.registerItemModels();
	}
}
