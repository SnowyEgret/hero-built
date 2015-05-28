package ds.plato.proxy;

import java.util.List;

import ds.plato.Plato;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Spell;
import ds.plato.item.staff.IStaff;
import ds.plato.item.staff.Staff;
import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class CommonProxy {

	public void setCustomRenderers(ISelect selectionManager, IPick pickManager, Iterable<Staff> staffs, Iterable<Spell> spells) {
	}

	public void registerEventHandlers(Plato plato, ISelect select, IUndo undo, IPick pick) {
	}

	public void setCustomRenderers(ISelect selectionManager, IPick pickManager, List<IStaff> staffs, List<ISpell> spells) {
		// TODO Auto-generated method stub
		
	}
}
