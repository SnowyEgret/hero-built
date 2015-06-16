package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;

public class SpellSelectEdge extends AbstractSpellSelect {

	public SpellSelectEdge() {
		super(Select.HORIZONTAL_NO_CORNERS);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IPlayer player) {
		IPick pickManager = player.getPickManager();
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
		super.invoke(player);
	}
	
}
