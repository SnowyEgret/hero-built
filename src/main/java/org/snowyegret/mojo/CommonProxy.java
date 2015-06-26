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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.item.IItem;
import org.snowyegret.mojo.item.spell.Spell;
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
				return "MoJo spells";
			}
		};
	}
	protected List<Item> items = Lists.newArrayList();

	public void registerEventHandlers() {
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
			items.add(initItem(Spell.class));
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
		// http://www.minecraftforge.net/forum/index.php?topic=20135.0
		MoJo.network = NetworkRegistry.INSTANCE.newSimpleChannel("plato");
		MoJo.network.registerMessage(KeyMessageHandler.class, KeyMessage.class, 0, Side.SERVER);
		MoJo.network.registerMessage(ClearManagersMessageHandler.class, ClearManagersMessage.class, 1, Side.SERVER);
		MoJo.network.registerMessage(SelectionMessageHandler.class, SelectionMessage.class, 2, Side.CLIENT);
		MoJo.network.registerMessage(PickMessageHandler.class, PickMessage.class, 3, Side.CLIENT);
		MoJo.network.registerMessage(SetBlockStateMessageHandler.class, SetBlockStateMessage.class, 4, Side.SERVER);
		MoJo.network.registerMessage(MouseClickMessageHandler.class, MouseClickMessage.class, 5, Side.SERVER);
	}

	public void setCustomStateMappers() {
	}

	public void registerGuiHandler() {
	}

	private Block initBlock(Block block) {
		// String classname = block.getClass().getSimpleName();
		// String name = classname.substring(0, 1).toLowerCase() + classname.substring(1);
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
				allSpells.addAll(list);
			}
			constructor = itemClass.getConstructor(List.class);
			item = (Item) constructor.newInstance(allSpells);
		}
		item.setUnlocalizedName(name);
		item.setMaxStackSize(1);
		// We are using this method to load class Staff to use it's model as a base model.
		// We don't want it to be part of the game
		if (item.getClass() != Staff.class && item.getClass() != Spell.class) {
			item.setCreativeTab(tabSpells);
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
