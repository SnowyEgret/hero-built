package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.condition.ICondition;
import org.snowyegret.mojo.item.spell.condition.IsOnEdgeOnCeiling;
import org.snowyegret.mojo.item.spell.condition.IsOnEdgeOnGround;
import org.snowyegret.mojo.item.spell.condition.IsOnExteriorEdge;
import org.snowyegret.mojo.player.Player;

import com.google.common.collect.Lists;

public class SpellSelectEdge extends AbstractSpellSelect {

	public SpellSelectEdge() {
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(Player player) {
		boolean allExteriorEdges = player.getModifiers().isPressed(Modifier.SHIFT);
		EnumFacing side = player.getPickManager().firstPick().side;
		BlockPos[] pattern = null;
		ICondition condition = null;
		if (allExteriorEdges) {
			pattern = Select.XYZ;
			condition = new IsOnExteriorEdge();
		} else {
			pattern = Select.HORIZONTAL_NO_CORNERS;
			// TODO SpellSelectEdge should work on any surface (not just ceiling or floor) #230
			// pattern = Select.XYZ;
			// condition = IsOnEdge(side);
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
		}
		select(player, pattern, Lists.newArrayList(condition));
	}

}
