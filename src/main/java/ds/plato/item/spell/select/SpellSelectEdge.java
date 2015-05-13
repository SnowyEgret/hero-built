package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.player.HotbarSlot;

public class SpellSelectEdge extends AbstractSpellSelect {

	public SpellSelectEdge(IUndo undo, ISelect select, IPick pick) {
		//super(null, undo, select, pick);
		//super(Shell.Type.ALL, undo, select, pick);
		super(Select.all, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		EnumFacing side = pickManager.getPicks()[0].side;
		if (side == EnumFacing.UP) {
			//positions = Select.edge();
		} else if (side == EnumFacing.DOWN) {
			//positions = Select.edgeUnder();
		} else {
			System.out.println("Got unexpected side=" + side);
		}
		super.invoke(world, slotEntries);
	}
}
