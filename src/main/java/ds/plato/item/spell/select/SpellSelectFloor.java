package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellSelectFloor extends AbstractSpellSelect {

	public SpellSelectFloor(IUndo undo, ISelect select, IPick pick) {
		super(Select.horizontal, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slots) {
		EnumFacing side = pickManager.getPicks()[0].side;
		switch (side) {
		case UP:
			setConditions(new IsOnGround());
			break;
		case DOWN:
			setConditions(new IsOnCeiling());
			break;
		default:
			return;
		}
		super.invoke(world, slots);
	}
}
