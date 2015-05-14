package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.player.HotbarSlot;

public class SpellSelectEdge extends AbstractSpellSelect {

	public SpellSelectEdge(IUndo undo, ISelect select, IPick pick) {
		super(Select.horizontalNoCorners, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {
		EnumFacing side = pickManager.getPicks()[0].side;
		switch (side) {
		case UP:
			setConditions(new IsOnEdgeOnGround());
			break;
		case DOWN:
			setConditions(new IsOnEdgeOnCeiling());
			break;
		default:
			return;
		}
		super.invoke(world, slots);
	}
}
