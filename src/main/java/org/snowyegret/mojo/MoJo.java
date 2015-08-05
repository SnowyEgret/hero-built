package org.snowyegret.mojo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSaved;
import org.snowyegret.mojo.block.BlockSelected;

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
	public static BlockSaved blockSaved;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println("Preinitializing--------------------------------------------------------------------------------------------------------");
		proxy.registerBlocks();
		proxy.registerItems();
		proxy.registerNetworkMessages();
		proxy.setCustomStateMappers();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println("Intializing------------------------------------------------------------------------------------------------------------");
		proxy.registerGuiHandler();
		proxy.registerEventHandlers();
		proxy.registerTileEntities();
		proxy.registerItemModels();
		proxy.registerItemBlockModels();
	}
	
	@EventHandler
	public void init(FMLPostInitializationEvent event) {
		System.out.println("Postinitializing--------------------------------------------------------------------------------------------------------");
	}
}
