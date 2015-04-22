package ds.plato;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
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
import ds.plato.network.SetBlockMessage;
import ds.plato.network.SetBlockMessageHandler;
import ds.plato.pick.PickManager;
import ds.plato.proxy.CommonProxy;
import ds.plato.select.SelectionManager;
import ds.plato.undo.UndoManager;

@Mod(modid = Plato.ID, name = Plato.NAME, version = Plato.VERSION)
public class Plato {

	public static final String ID = "plato";
	public static final String NAME = "Plato";
	public static final String VERSION = "0.5";

	@Instance(ID) public static Plato instance;
	@SidedProxy(clientSide = "ds.plato.proxy.ClientProxy", serverSide = "ds.plato.proxy.CommonProxy") public static CommonProxy proxy;

	private static IUndo undoManager;
	private static ISelect selectionManager;
	private static IPick pickManager;
	private Configuration configuration;
	private List<Spell> spells;
	private List<Staff> staffs;
	public static SimpleNetworkWrapper network;
	public static Logger log;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		log = LogManager.getLogger(NAME);

		log.info("[Plato.preInit]Initializing blocks...");
		Block blockSelected = initBlock(new BlockSelected());
		Block blockPicked = initBlock(new BlockPicked());

		// Try loading a Wavefront model
		// Block blockModel = initBlock(new BlockModel());
		// blockModel.setCreativeTab(CreativeTabs.tabBlock);
		// TODO what is this stringID
		// GameRegistry.registerTileEntity(BlockModelTileEntity.class, "stringID");

		undoManager = new UndoManager();
		selectionManager = new SelectionManager(blockSelected);
		pickManager = new PickManager(blockPicked);

		log.info("[Plato.preInit] Initializing spells and staffs");
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		SpellLoader loader = new SpellLoader(configuration, undoManager, selectionManager, pickManager, ID);
		try {
//			spells = loader.loadSpellsFromPackage("ds.plato.item.spell");
//			log.info("[Plato.preInit] loaded spells=" + spells);
//
//			Staff selectionStaff = loader.loadStaff(StaffSelect.class);
//			Staff transformStaff = loader.loadStaff(StaffTransform.class);
//			Staff drawStaff = loader.loadStaff(StaffDraw.class);
//
//			for (Spell s : spells) {
//				if (s instanceof AbstractSpellSelect) {
//					selectionStaff.addSpell(s);
//				} else if (s instanceof AbstractSpellTransform || s instanceof AbstractSpellMatrix) {
//					transformStaff.addSpell(s);
//				} else if (s instanceof AbstractSpellDraw) {
//					drawStaff.addSpell(s);
//				}
//			}
//
//			log.info("[Plato.preInit] selectionStaff=" + selectionStaff);
//			log.info("[Plato.preInit] transformStaff=" + transformStaff);
//			log.info("[Plato.preInit] drawStaff=" + drawStaff);
			
			spells = new ArrayList<>();
			List<Spell> drawSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.draw");
			List<Spell> selectSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.select");
			List<Spell> transformSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.transform");
			List<Spell> otherSpells = loader.loadSpellsFromPackage("ds.plato.item.spell.other");
			spells.addAll(drawSpells);
			spells.addAll(selectSpells);
			spells.addAll(transformSpells);
			spells.addAll(otherSpells);

			// Create some empty staffs. For now, they have a different base class.
			staffs = new ArrayList<>();
			staffs.add(loader.loadStaff(StaffOak.class));
			staffs.add(loader.loadStaff(StaffBirch.class));
			staffs.add(loader.loadStaff(StaffAcacia.class));

			staffs.add(loader.loadStaffPreset(StaffDraw.class, drawSpells));
			staffs.add(loader.loadStaffPreset(StaffSelect.class, selectSpells));
			staffs.add(loader.loadStaffPreset(StaffTransform.class, transformSpells));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		configuration.save();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		// http://www.minecraftforge.net/forum/index.php?topic=20135.0
		network = NetworkRegistry.INSTANCE.newSimpleChannel("plato");
		network.registerMessage(SetBlockMessageHandler.class, SetBlockMessage.class, 0, Side.SERVER);

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.setCustomRenderers(selectionManager, pickManager, staffs, spells);
		proxy.registerEventHandlers(this, selectionManager, undoManager, pickManager);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		log.info(NAME + " " + VERSION + " loaded.");
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		log.info("[Plato.serverStarted]");
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		log.info("[Plato.serverStopping]");
		selectionManager.clearSelections();
		pickManager.clearPicks();
	}

	private Block initBlock(Block block) {
		String classname = block.getClass().getSimpleName();
		String name = classname.substring(0, 1).toLowerCase() + classname.substring(1);
		block.setUnlocalizedName(name);
		//block.setBlockTextureName(ID + ":" + name);
		GameRegistry.registerBlock(block, ID + name);
		return block;
	}
}
