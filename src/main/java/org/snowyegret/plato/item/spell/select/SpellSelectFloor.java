package org.snowyegret.plato.item.spell.select;

import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.undo.IUndo;

import net.minecraft.util.EnumFacing;

public class SpellSelectFloor extends AbstractSpellSelect {

	public SpellSelectFloor() {
		super(Select.HORIZONTAL);
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
