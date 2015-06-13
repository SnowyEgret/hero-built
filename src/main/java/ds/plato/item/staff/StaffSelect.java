package ds.plato.item.staff;

import java.util.List;

import net.minecraft.init.Items;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;

public class StaffSelect extends StaffPreset {

	public StaffSelect(List<Spell> spells) {
		super(spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
