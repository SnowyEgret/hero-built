package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.player.IPlayer;

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
		EnumFacing side = player.getPickManager().firstPick().side;
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
