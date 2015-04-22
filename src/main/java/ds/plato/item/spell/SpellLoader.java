package ds.plato.item.spell;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.item.staff.Staff;
import ds.plato.item.staff.StaffPreset;
import ds.plato.util.StringUtils;

public class SpellLoader {

	public static CreativeTabs tabSpells;
	private String modId;
	IUndo undoManager;
	ISelect selectionManager;
	IPick pickManager;
	private Configuration config;

	public SpellLoader(
			Configuration config,
			IUndo undoManager,
			ISelect selectionManager,
			IPick pickManager,
			String modId) {
		this.undoManager = undoManager;
		this.selectionManager = selectionManager;
		this.pickManager = pickManager;
		this.modId = modId;
		this.config = config;

		tabSpells = new CreativeTabs("tabSpells") {
			@Override
			public Item getTabIconItem() {
				return Items.glass_bottle;
			}
		};
	}

	//For now, duplicate method loadStaff()
	public Staff loadStaff(Class<? extends Staff> staffClass) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		System.out.print("[SpellLoader.loadStaff] Loading staff " + staffClass.getSimpleName() + "...");
		String name = StringUtils.toCamelCase(staffClass);
		Constructor c = staffClass.getConstructor(IPick.class);
		Staff s = (Staff) c.newInstance(pickManager);
		s.setUnlocalizedName(name);
		s.setMaxStackSize(1);
		s.setCreativeTab(tabSpells);
		//s.setTextureName(modId + ":staff");
		GameRegistry.registerItem(s, name);
		if (s.hasRecipe()) {
			GameRegistry.addRecipe(new ItemStack(s), s.getRecipe());
		}
		System.out.println("...done.");
		return s;
	}

	public StaffPreset loadStaffPreset(Class<? extends StaffPreset> staffClass, List<Spell> spells) throws NoSuchMethodException, SecurityException, MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
		System.out.print("[SpellLoader.loadStaffPreset] Loading staff " + staffClass.getSimpleName() + "...");
		String name = StringUtils.toCamelCase(staffClass);
		Constructor c = staffClass.getConstructor(IPick.class, List.class);
		StaffPreset s = (StaffPreset) c.newInstance(pickManager, spells);
		s.setUnlocalizedName(name);
		s.setMaxStackSize(1);
		s.setCreativeTab(tabSpells);
		//s.setTextureName(modId + ":staff");
		GameRegistry.registerItem(s, name);
		if (s.hasRecipe()) {
			GameRegistry.addRecipe(new ItemStack(s), s.getRecipe());
		}
		System.out.println("...done.");
		return s;
	}

	public List<Spell> loadSpellsFromPackage(String packageName) throws MalformedURLException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

		ClassPath p = ClassPath.from(this.getClass().getClassLoader());
		List<Spell> spells = new ArrayList<>();
		for (ClassInfo i : p.getTopLevelClassesRecursive(packageName)) {
			Class c = i.load();
			if (Spell.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
				spells.add(loadSpell(c));
			}
		}
		return spells;
	}
	
	private Spell loadSpell(Class<? extends Spell> spellClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
	
		System.out.print("[SpellLoader.loadSpell] Loading spell=" + spellClass.getSimpleName() + "...");
		String name = StringUtils.toCamelCase(spellClass);
		Constructor<? extends Spell> c = spellClass.getConstructor(IUndo.class, ISelect.class, IPick.class);
		Spell s = (Spell) c.newInstance(undoManager, selectionManager, pickManager);
		s.setUnlocalizedName(name);
		s.setMaxStackSize(1);
		s.setCreativeTab(tabSpells);
		//s.setTextureName(modId + ":spell");
		//Can't remember why I did this. For SpellRestore?
		GameRegistry.registerItem(s, s.getClass().getSimpleName());
		//GameRegistry.registerItem(s, name);
		if (s.hasRecipe()) {
			//System.out.println("[SpellLoader.loadSpell] s.getRecipe()=" + s.getRecipe());
			GameRegistry.addRecipe(new ItemStack(s), s.getRecipe());
		}
		System.out.println("...done.");
		return s;
	}
}
