package org.snowyegret.mojo.item.staff;

import java.util.List;

import net.minecraft.init.Items;

import org.snowyegret.mojo.item.spell.Spell;

public class StaffSelect extends StaffPreset {

	public StaffSelect(List<Spell> spells) {
		super(spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
