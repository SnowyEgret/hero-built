package ds.plato.item.staff;

import java.util.List;

import net.minecraft.init.Items;
import ds.plato.api.IPick;
import ds.plato.item.spell.Spell;

public class StaffSelect extends StaffPreset {

	public StaffSelect(IPick pickManager, List<Spell> spells) {
		super(pickManager, spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
