package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellSelectEdge extends AbstractSpellSelect {

	public SpellSelectEdge(IUndo undo, ISelect select, IPick pick) {
		super(Select.horizontalNoCorners, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		EnumFacing side = pickManager.getPicks()[0].side;
		switch (side) {
		case UP:
			setConditions(new IsOnEdgeOnGround());
			break;
		case DOWN:
			setConditions(new IsOnEdgeOnCeiling());
			break;
			//$CASES-OMITTED$
		default:
			return;
		}
		super.invoke(world, player);
	}
	
}
