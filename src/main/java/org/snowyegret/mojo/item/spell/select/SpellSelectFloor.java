package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.condition.ICondition;
import org.snowyegret.mojo.item.spell.condition.IsOnCeiling;
import org.snowyegret.mojo.item.spell.condition.IsOnGround;
import org.snowyegret.mojo.player.Player;

import com.google.common.collect.Lists;

public class SpellSelectFloor extends AbstractSpellSelect {

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(Player player) {
		ICondition condition = null;
		EnumFacing side = player.getPickManager().firstPick().side;
		switch (side) {
		case UP:
			condition = new IsOnGround();
			break;
		case DOWN:
			condition = new IsOnCeiling();
			break;
		// $CASES-OMITTED$
		default:
			return;
		}
		select(player, Select.HORIZONTAL_NO_CORNERS, Lists.newArrayList(condition));
	}

}
