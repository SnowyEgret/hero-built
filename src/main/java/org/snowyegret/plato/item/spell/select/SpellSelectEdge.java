package org.snowyegret.plato.item.spell.select;

import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.player.IPlayer;

import net.minecraft.util.EnumFacing;

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
		EnumFacing side = pickManager.firstPick().side;
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
