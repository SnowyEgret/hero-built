package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.player.Player;

public class SpellSelectFloor extends AbstractSpellSelect {

	public SpellSelectFloor() {
		super(Select.HORIZONTAL);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(Player player) {
		EnumFacing side = player.getPickManager().firstPick().side;
		switch (side) {
		case UP:
			setConditions(new IsOnGround());
			break;
		case DOWN:
			setConditions(new IsOnCeiling());
			break;
			//$CASES-OMITTED$
		default:
			return;
		}
		super.invoke(player);
	}
	
}
