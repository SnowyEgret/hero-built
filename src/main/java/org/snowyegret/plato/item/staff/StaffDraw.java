package org.snowyegret.plato.item.staff;

import java.util.List;

import org.snowyegret.plato.item.spell.Spell;
import org.snowyegret.plato.pick.IPick;

import net.minecraft.init.Items;

public class StaffDraw extends StaffPreset {

	public StaffDraw(List<Spell> spells) {
		super(spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
