package org.snowyegret.mojo;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.gui.PickInfo;
import org.snowyegret.mojo.gui.SelectionInfo;
import org.snowyegret.mojo.item.ModelResourceLocations;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.SpellLoader;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.item.staff.StaffAcacia;
import org.snowyegret.mojo.item.staff.StaffBirch;
import org.snowyegret.mojo.item.staff.StaffDraw;
import org.snowyegret.mojo.item.staff.StaffOak;
import org.snowyegret.mojo.item.staff.StaffSelect;
import org.snowyegret.mojo.item.staff.StaffTransform;
import org.snowyegret.mojo.network.ClearManagersMessage;
import org.snowyegret.mojo.network.ClearManagersMessageHandler;
import org.snowyegret.mojo.network.KeyMessage;
import org.snowyegret.mojo.network.KeyMessageHandler;
import org.snowyegret.mojo.network.MouseClickMessage;
import org.snowyegret.mojo.network.MouseClickMessageHandler;
import org.snowyegret.mojo.network.PickMessage;
import org.snowyegret.mojo.network.PickMessageHandler;
import org.snowyegret.mojo.network.SelectionMessage;
import org.snowyegret.mojo.network.SelectionMessageHandler;
import org.snowyegret.mojo.network.SetBlockStateMessage;
import org.snowyegret.mojo.network.SetBlockStateMessageHandler;
import org.snowyegret.mojo.proxy.CommonProxy;

import com.google.common.collect.Lists;

@Mod(modid = MoJo.ID, name = MoJo.NAME, version = MoJo.VERSION)
public class MoJo {

	public static final String ID = "mojo";
	public static final String NAME = "MoJo";
	public static final String VERSION = "0.5";
	public static final String PACKAGE = "org.snowyegret.mojo";

	@Instance(ID)
	public static MoJo instance;
	@SidedProxy(clientSide = PACKAGE + ".proxy.ClientProxy", serverSide = PACKAGE + ".proxy.CommonProxy")
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper network;

	public static BlockSelected blockSelected;
	public static BlockPicked blockPicked;

	public static SelectionInfo selectionInfo = new SelectionInfo();
	public static PickInfo pickInfo = new PickInfo();
	private List<Item>  spells = Lists.newArrayList();
	private List<Item>  staffs = Lists.newArrayList();

	// private Configuration configuration;

	public void setSelectionInfo(SelectionInfo selectionInfo) {
		this.selectionInfo = selectionInfo;
	}

	public void setPickInfo(PickInfo pickInfo) {
		this.pickInfo = pickInfo;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		System.out.println("Initializing blocks...");
		blockSelected = (BlockSelected) initBlock(new BlockSelected());
		blockPicked = (BlockPicked) initBlock(new BlockPicked());

		// configuration = new Configuration(event.getSuggestedConfigurationFile());
		SpellLoader loader = new SpellLoader();
		try {
			System.out.println("Initializing spells...");
			List<Spell> drawSpells = loader.loadSpellsFromPackage(PACKAGE + ".item.spell.draw");
			List<Spell> selectSpells = loader.loadSpellsFromPackage(PACKAGE + ".item.spell.select");
			List<Spell> transformSpells = loader.loadSpellsFromPackage(PACKAGE + ".item.spell.transform");
			List<Spell> matrixSpells = loader.loadSpellsFromPackage(PACKAGE + ".item.spell.matrix");
			List<Spell> otherSpells = loader.loadSpellsFromPackage(PACKAGE + ".item.spell.other");
			spells.addAll(drawSpells);
			spells.addAll(selectSpells);
			spells.addAll(matrixSpells);
			spells.addAll(transformSpells);
			spells.addAll(otherSpells);

			System.out.println("Initializing staffs...");
			// Create some empty staffs. For now, they have a different base class.
			staffs.add(loader.loadStaff(StaffOak.class));
			staffs.add(loader.loadStaff(StaffBirch.class));
			staffs.add(loader.loadStaff(StaffAcacia.class));

			staffs.add(loader.loadStaffPreset(StaffDraw.class, drawSpells));
			staffs.add(loader.loadStaffPreset(StaffSelect.class, selectSpells));
			staffs.add(loader.loadStaffPreset(StaffTransform.class, transformSpells, matrixSpells));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		// configuration.save();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		// http://www.minecraftforge.net/forum/index.php?topic=20135.0
		network = NetworkRegistry.INSTANCE.newSimpleChannel("plato");
		network.registerMessage(KeyMessageHandler.class, KeyMessage.class, 0, Side.SERVER);
		network.registerMessage(ClearManagersMessageHandler.class, ClearManagersMessage.class, 1, Side.SERVER);
		network.registerMessage(SelectionMessageHandler.class, SelectionMessage.class, 2, Side.CLIENT);
		network.registerMessage(PickMessageHandler.class, PickMessage.class, 3, Side.CLIENT);
		network.registerMessage(SetBlockStateMessageHandler.class, SetBlockStateMessage.class, 4, Side.SERVER);
		network.registerMessage(MouseClickMessageHandler.class, MouseClickMessage.class, 5, Side.SERVER);

		// Create custom state mappers for BlockSelected and BlockPicked models
		ModelLoader.setCustomStateMapper(blockSelected, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				//return BlockSelected.modelResourceLocation;
				return ModelResourceLocations.get(BlockSelected.class);
			}
		});
		ModelLoader.setCustomStateMapper(blockPicked, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				//return BlockPicked.modelResourceLocation;
				return ModelResourceLocations.get(BlockPicked.class);
			}
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// TODO move network registry stuff in preInit here
		// proxy.registerNetworkMessages();
		proxy.registerEventHandlers();
		proxy.registerTileEntities();
		proxy.registerItemModels(staffs);
		
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
	}

	// Private----------------------------------------------------------------------

	private Block initBlock(Block block) {
		String classname = block.getClass().getSimpleName();
		String name = classname.substring(0, 1).toLowerCase() + classname.substring(1);
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, name);
		return block;
	}

}
