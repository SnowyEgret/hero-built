package org.snowyegret.mojo.item;

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
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.item.staff.StaffPreset;
import org.snowyegret.mojo.util.StringUtils;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class ItemLoader {

	public static CreativeTabs tabSpells;

	public ItemLoader() {

		tabSpells = new CreativeTabs("tabSpells") {
			@Override
			public Item getTabIconItem() {
				return Items.glass_bottle;
			}
		};
	}

	public Staff loadStaff(Class<? extends Staff> staffClass) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		System.out.println("Loading staff " + staffClass.getSimpleName());
		String name = StringUtils.toCamelCase(staffClass);
		Constructor c = staffClass.getConstructor();
		Staff s = (Staff) c.newInstance();
		s.setUnlocalizedName(name);
		s.setMaxStackSize(1);
		// We are using this method to load class Staff to use it's model as a base model.
		// We don't want it to be part of the game
		if (s.getClass() != Staff.class) {
			s.setCreativeTab(tabSpells);
		}
		GameRegistry.registerItem(s, name);
		if (s.hasRecipe()) {
			GameRegistry.addRecipe(new ItemStack(s), s.getRecipe());
		}
		return s;
	}

	public StaffPreset loadStaffPreset(Class<? extends StaffPreset> staffClass, List<Spell>... spells) throws NoSuchMethodException, SecurityException, MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
		// Combine varyarg spells into one list
		List<Spell> allSpells = new ArrayList();
		for (List<Spell> list : spells) {
			allSpells.addAll(list);
		}
		System.out.println("Loading staff " + staffClass.getSimpleName());
		String name = StringUtils.toCamelCase(staffClass);
		Constructor c = staffClass.getConstructor(List.class);
		StaffPreset s = (StaffPreset) c.newInstance(allSpells);
		s.setUnlocalizedName(name);
		s.setMaxStackSize(1);
		s.setCreativeTab(tabSpells);
		GameRegistry.registerItem(s, name);
		if (s.hasRecipe()) {
			GameRegistry.addRecipe(new ItemStack(s), s.getRecipe());
		}
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

	public Spell loadSpell(Class<? extends Spell> spellClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

		System.out.println("Loading spell " + spellClass.getSimpleName());
		String name = StringUtils.toCamelCase(spellClass);
		Constructor<? extends Spell> c = spellClass.getConstructor();
		Spell s = c.newInstance();
		s.setUnlocalizedName(name);
		s.setMaxStackSize(1);
		// We are using this method to load class Spell to use it's model as a base model.
		// We don't want it to be part of the game
		if (s.getClass() != Spell.class) {
			s.setCreativeTab(tabSpells);
		}
		GameRegistry.registerItem(s, name);
		if (s.hasRecipe()) {
			GameRegistry.addRecipe(new ItemStack(s), s.getRecipe());
		}
		return s;
	}
}
