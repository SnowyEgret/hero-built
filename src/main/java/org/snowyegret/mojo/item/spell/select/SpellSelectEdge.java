package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.ICondition;
import org.snowyegret.mojo.player.Player;

import com.google.common.collect.Lists;

public class SpellSelectEdge extends AbstractSpellSelect {

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(Player player) {
		EnumFacing side = player.getPickManager().firstPick().side;
		ICondition condition = null;
		switch (side) {
		case UP:
			condition = new IsOnEdgeOnGround();
			break;
		case DOWN:
			condition = new IsOnEdgeOnCeiling();
			break;
		// $CASES-OMITTED$
		default:
			System.out.println("Pick either the top or bottom face of a block");
			// TODO Method Player.sendMessge(String message) #222
			// player.sendMessage("item.spellSelectEdge.message.topOrBottomFace");
			return;
		}
		select(player, Select.HORIZONTAL_NO_CORNERS, Lists.newArrayList(condition));
	}

}
