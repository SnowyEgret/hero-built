package ds.plato;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockSelected;
import ds.plato.gui.GuiHandler;
import ds.plato.gui.PickInfo;
import ds.plato.gui.SelectionInfo;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.SpellLoader;
import ds.plato.item.staff.Staff;
import ds.plato.item.staff.StaffAcacia;
import ds.plato.item.staff.StaffBirch;
import ds.plato.item.staff.StaffDraw;
import ds.plato.item.staff.StaffOak;
import ds.plato.item.staff.StaffSelect;
import ds.plato.item.staff.StaffTransform;
import ds.plato.network.ClearManagersMessage;
import ds.plato.network.ClearManagersMessageHandler;
import ds.plato.network.KeyMessage;
import ds.plato.network.KeyMessageHandler;
import ds.plato.network.MouseClickMessage;
import ds.plato.network.MouseClickMessageHandler;
import ds.plato.network.PickMessage;
import ds.plato.network.PickMessageHandler;
import ds.plato.network.SelectionMessage;
import ds.plato.network.SelectionMessageHandler;
import ds.plato.network.SetBlockStateMessage;
import ds.plato.network.SetBlockStateMessageHandler;
import ds.plato.proxy.CommonProxy;

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
	public static boolean forceMessaging = false;

	public static BlockSelected blockSelected;
	public static BlockPicked blockPicked;

	// TODO Remove SetBlockStateDoneMessage and handler #122
	public static boolean setBlockMessageDone = false;
	public static SelectionInfo selectionInfo = new SelectionInfo();
	public static PickInfo pickInfo = new PickInfo();
	// private Configuration configuration;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		List<Spell> spells = new ArrayList<>();
		List<Staff> staffs = new ArrayList<>();

		String prop = System.getProperties().getProperty("plato.forceMessaging");
		if (prop != null) {
			if (prop.equals("true")) {
				forceMessaging = true;
			}
		}

		//TODO BlockSelected and picked will have to have a tileEntity
		System.out.println("Initializing blocks...");
		blockSelected = (BlockSelected) initBlock(new BlockSelected());
		blockPicked = (BlockPicked) initBlock(new BlockPicked());

		System.out.println("Initializing spells and staffs...");
		// configuration = new Configuration(event.getSuggestedConfigurationFile());
		SpellLoader loader = new SpellLoader();
		try {
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
		// proxy.setCustomRenderers(selectionManager, pickManager, staffs, spells);
		// TODO could pass MouseHandler here to avoid static reference to isOrbiting
		proxy.registerEventHandlers();
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

	public void setSelectionInfo(SelectionInfo selectionInfo) {
		this.selectionInfo = selectionInfo;
		
	}

	public void setPickInfo(PickInfo pickInfo) {
		this.pickInfo = pickInfo;
	}
}
