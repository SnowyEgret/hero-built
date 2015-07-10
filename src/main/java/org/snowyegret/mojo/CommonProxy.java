package org.snowyegret.mojo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.event.EventHandlerClient;
import org.snowyegret.mojo.event.EventHandlerServer;
import org.snowyegret.mojo.event.KeyHandler;
import org.snowyegret.mojo.event.MouseHandler;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.item.IItem;
import org.snowyegret.mojo.item.ItemBase;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.draw.SpellDivide;
import org.snowyegret.mojo.item.spell.draw.SpellSpline;
import org.snowyegret.mojo.item.spell.transform.SpellDelete;
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
import org.snowyegret.mojo.network.OpenGuiMessage;
import org.snowyegret.mojo.network.OpenGuiMessageHandler;
import org.snowyegret.mojo.network.PickMessage;
import org.snowyegret.mojo.network.PickMessageHandler;
import org.snowyegret.mojo.network.SelectionMessage;
import org.snowyegret.mojo.network.SelectionMessageHandler;
import org.snowyegret.mojo.network.SpellMessage;
import org.snowyegret.mojo.network.SpellMessageHandler;
import org.snowyegret.mojo.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class CommonProxy {

	private static CreativeTabs tabSpells;
	static {
		tabSpells = new CreativeTabs("tabSpells") {
			@Override
			public Item getTabIconItem() {
				return Items.glass_bottle;
			}

			@Override
			public String getTranslatedTabLabel() {
				// TODO Auto-generated method stub
				return super.getTranslatedTabLabel();
			}

			@Override
			public String getTabLabel() {
				return "Mo'Jo";
			}
		};
	}

	protected List<Item> items = Lists.newArrayList();

	private final List<Class<? extends ItemBase>> itemsExcluded = Lists.newArrayList(Staff.class, SpellDelete.class,
			SpellDivide.class, SpellSpline.class, SpellDivide.class);

	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());
		MinecraftForge.EVENT_BUS.register(new MouseHandler());
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(PrevStateTileEntity.class, PrevStateTileEntity.class.getSimpleName());
	}

	public void registerItemModels() {
	}

	public void registerBlocks() {
		System.out.println("Initializing blocks...");
		MoJo.blockSelected = (BlockSelected) initBlock(new BlockSelected());
		MoJo.blockPicked = (BlockPicked) initBlock(new BlockPicked());
	}

	public void registerItems() {
		try {
			System.out.println("Initializing spells...");
			List<Spell> drawSpells = initSpellsFromPackage("draw");
			List<Spell> selectSpells = initSpellsFromPackage("select");
			List<Spell> transformSpells = initSpellsFromPackage("transform");
			List<Spell> matrixSpells = initSpellsFromPackage("matrix");
			List<Spell> otherSpells = initSpellsFromPackage("other");

			// We are initializing Spell so that we can use its model as a base model
			// items.add(initItem(Spell.class));
			items.addAll(drawSpells);
			items.addAll(selectSpells);
			items.addAll(matrixSpells);
			items.addAll(transformSpells);
			items.addAll(otherSpells);

			System.out.println("Initializing staffs...");
			// We are initializing Staff so that we can use its model as a base model
			items.add(initItem(Staff.class));
			items.add(initItem(StaffOak.class));
			items.add(initItem(StaffBirch.class));
			items.add(initItem(StaffAcacia.class));

			items.add(initItem(StaffDraw.class, drawSpells));
			items.add(initItem(StaffSelect.class, selectSpells));
			items.add(initItem(StaffTransform.class, transformSpells, matrixSpells));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public void registerNetworkMessages() {
		MoJo.network = NetworkRegistry.INSTANCE.newSimpleChannel(MoJo.MODID);

		// Messages to server
		MoJo.network.registerMessage(KeyMessageHandler.class, KeyMessage.class, 0, Side.SERVER);
		MoJo.network.registerMessage(ClearManagersMessageHandler.class, ClearManagersMessage.class, 1, Side.SERVER);
		MoJo.network.registerMessage(MouseClickMessageHandler.class, MouseClickMessage.class, 2, Side.SERVER);

		// Messages to client
		MoJo.network.registerMessage(SelectionMessageHandler.class, SelectionMessage.class, 10, Side.CLIENT);
		MoJo.network.registerMessage(PickMessageHandler.class, PickMessage.class, 11, Side.CLIENT);
		MoJo.network.registerMessage(SpellMessageHandler.class, SpellMessage.class, 12, Side.CLIENT);
		MoJo.network.registerMessage(OpenGuiMessageHandler.class, OpenGuiMessage.class, 13, Side.CLIENT);
	}

	public void setCustomStateMappers() {
	}

	public void registerGuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MoJo.instance, new GuiHandler());
	}

	// Private------------------------------------------------------------------------------------

	private Block initBlock(Block block) {
		String name = StringUtils.nameFor(block.getClass());
		System.out.println("Intitializing block " + name);
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, name);
		return block;
	}

	private Item initItem(Class<? extends Item> itemClass) throws Exception {
		return initItem(itemClass, (List<Spell>[]) null);
	}

	private Item initItem(Class<? extends Item> itemClass, List<Spell>... spellsLists) throws Exception {
		String name = StringUtils.nameFor(itemClass);
		System.out.println("Intitializing item " + name);
		Constructor constructor = null;
		Item item = null;
		if (spellsLists == null) {
			constructor = itemClass.getConstructor();
			item = (Item) constructor.newInstance();
		} else {
			List<Spell> allSpells = new ArrayList();
			for (List<Spell> list : spellsLists) {
				for (Spell spell : list) {
					if (!itemsExcluded.contains(spell.getClass())) {
						allSpells.add(spell);
					} else {
						System.out.println("Excluded " + spell + " from staff " + name);
					}
				}
			}
			constructor = itemClass.getConstructor(List.class);
			item = (Item) constructor.newInstance(allSpells);
		}
		item.setUnlocalizedName(name);
		item.setMaxStackSize(1);
		// We are using this method to load class Staff to use it's model as a base model.
		// We don't want it to be part of the game
		if (!itemsExcluded.contains(item.getClass())) {
			item.setCreativeTab(tabSpells);
		} else {
			System.out.println("Excluded " + name + " from creative tabs");
		}
		GameRegistry.registerItem(item, name);
		if (((IItem) item).hasRecipe()) {
			GameRegistry.addRecipe(new ItemStack(item), ((IItem) item).getRecipe());
		}
		return item;
	}

	private List<Spell> initSpellsFromPackage(String packageName) throws Exception {

		ClassPath path = ClassPath.from(this.getClass().getClassLoader());
		List<Spell> spells = Lists.newArrayList();
		for (ClassInfo i : path.getTopLevelClassesRecursive(MoJo.DOMAIN + ".item.spell." + packageName)) {
			Class c = i.load();
			if (Spell.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
				spells.add((Spell) initItem(c));
			}
		}
		return spells;
	}
}
