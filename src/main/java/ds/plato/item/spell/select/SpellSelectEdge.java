package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;

public class SpellSelectEdge extends AbstractSpellSelect {

	public SpellSelectEdge(IUndo undo, ISelect select, IPick pick) {
		//super(null, undo, select, pick);
		super(Shell.Type.ALL, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		EnumFacing side = pickManager.getPicks()[0].side;
		System.out.println(side);
		if (side == EnumFacing.UP) {
			shellType = Shell.Type.EDGE;
		} else if (side == EnumFacing.DOWN) {
			shellType = Shell.Type.EDGE_UNDER;
		} else {
			System.out.println("Got unexpected side=" + side);
		}
		super.invoke(world, slotEntries);
	}
}
