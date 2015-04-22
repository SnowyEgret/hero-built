package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;

public class SpellGrowFloor extends AbstractSpellSelect {

	public SpellGrowFloor(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.FLOOR, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		int side = pickManager.getPicks()[0].side;
		System.out.println("[SpellGrowFloor.invoke] side=" + side);
		if (side == 0) {
				shellType = Shell.Type.CEILING;
		} else if (side == 1) {
				shellType = Shell.Type.FLOOR;
		} else {
			System.out.println("[SpellGrowFloor.invoke] Got unexpected side=" + side);
		}
		super.invoke(world, slotEntries);
	}
}
