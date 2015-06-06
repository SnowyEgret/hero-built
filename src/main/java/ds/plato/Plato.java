package ds.plato;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockSelected;
import ds.plato.gui.GuiHandler;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.SpellLoader;
import ds.plato.item.staff.Staff;
import ds.plato.item.staff.StaffAcacia;
import ds.plato.item.staff.StaffBirch;
import ds.plato.item.staff.StaffDraw;
import ds.plato.item.staff.StaffOak;
import ds.plato.item.staff.StaffSelect;
import ds.plato.item.staff.StaffTransform;
import ds.plato.network.NextSpellMessage;
import ds.plato.network.NextSpellMessageHandler;
import ds.plato.network.PrevSpellMessage;
import ds.plato.network.PrevSpellMessageHandler;
import ds.plato.network.SetBlockMessage;
import ds.plato.network.SetBlockMessageHandler;
import ds.plato.network.SetBlockStateMessage;
import ds.plato.network.SetBlockStateMessageHandler;
import ds.plato.pick.IPick;
import ds.plato.pick.PickManager;
import ds.plato.player.Player;
import ds.plato.proxy.CommonProxy;
import ds.plato.select.ISelect;
import ds.plato.select.SelectionManager;
import ds.plato.undo.IUndo;
import ds.plato.undo.UndoManager;
import ds.plato.world.IWorld;

@Mod(modid = Plato.ID, name = Plato.NAME, version = Plato.VERSION)
public class Plato {

	public static final String ID = "plato";
	public static final String NAME = "Plato";
	public static final String VERSION = "0.5";

	@Instance(ID)
	public static Plato instance;
	@SidedProxy(clientSide = "ds.plato.proxy.ClientProxy", serverSide = "ds.plato.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper network;

	private static IUndo undoManager;
	private static ISelect selectionManager;
	private static IPick pickManager;
	private Configuration configuration;
	private List<Spell> spells;
	private List<Staff> staffs;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		System.out.println("Initializing blocks...");
		BlockSelected blockSelected = (BlockSelected) initBlock(new BlockSelected());
		BlockPicked blockPicked = (BlockPicked) initBlock(new BlockPicked());

		undoManager = new UndoManager();
		selectionManager = new SelectionManager(blockSelected);
		pickManager = new PickManager(blockPicked, selectionManager);

		blockSelected.setSelectionManager(selectionManager);
		blockPicked.setPickManager(pickManager);
		blockPicked.setSelectionManager(selectionManager);

		System.out.println("Initializing spells and staffs...");
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		SpellLoader loader = new SpellLoader(configuration, undoManager, selectionManager, pickManager, ID);
		try {

			spells = new ArrayList<>();
			List<Spell> drawSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.draw");
			List<Spell> selectSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.select");
			List<Spell> transformSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.transform");
			List<Spell> matrixSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.matrix");
			List<Spell> otherSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.other");
			spells.addAll(drawSpells);
			spells.addAll(selectSpells);
			spells.addAll(matrixSpells);
			spells.addAll(transformSpells);
			spells.addAll(otherSpells);

			// Create some empty staffs. For now, they have a different base class.
			staffs = new ArrayList<>();
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
		configuration.save();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		// http://www.minecraftforge.net/forum/index.php?topic=20135.0
		network = NetworkRegistry.INSTANCE.newSimpleChannel("plato");
		network.registerMessage(SetBlockMessageHandler.class, SetBlockMessage.class, 0, Side.SERVER);
		network.registerMessage(PrevSpellMessageHandler.class, PrevSpellMessage.class, 1, Side.SERVER);
		network.registerMessage(NextSpellMessageHandler.class, NextSpellMessage.class, 2, Side.SERVER);
		network.registerMessage(SetBlockStateMessageHandler.class, SetBlockStateMessage.class, 3, Side.SERVER);

		//Create custom state mappers for BlockSelected and BlockPicked models
		ModelLoader.setCustomStateMapper(blockSelected, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return BlockSelected.modelResourceLocation;
			}
		});
		ModelLoader.setCustomStateMapper(blockPicked, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return BlockPicked.modelResourceLocation;
			}
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//proxy.setCustomRenderers(selectionManager, pickManager, staffs, spells);
		//TODO could pass MouseHandler here to avoid static reference to isOrbiting
		proxy.registerEventHandlers(this, selectionManager, undoManager, pickManager);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println();
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		System.out.println();
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		System.out.println("Server stopping");
		IWorld world = Player.instance().getWorld();
		selectionManager.clearSelections(world);
		pickManager.clearPicks(world);
	}
	
	//Private----------------------------------------------------------------------

	private Block initBlock(Block block) {
		String classname = block.getClass().getSimpleName();
		String name = classname.substring(0, 1).toLowerCase() + classname.substring(1);
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, name);
		return block;
	}
}
