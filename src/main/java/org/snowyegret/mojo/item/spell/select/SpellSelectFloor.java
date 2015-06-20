package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.undo.IUndo;

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
