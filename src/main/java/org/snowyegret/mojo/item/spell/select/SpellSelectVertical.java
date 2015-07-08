package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

public class SpellSelectVertical extends AbstractSpellSelect {

	@Override
	public void invoke(Player player) {
		BlockPos[] pattern = null;
		switch (player.getVerticalPlane()) {
		case VERTICAL_XY_EAST_WEST:
			pattern = Select.EAST_WEST;
			break;
		case VERTICAL_YZ_NORTH_SOUTH:
			pattern = Select.NORTH_SOUTH;
			break;
		default:
			System.out.println("Unexpected plane: " + player.getVerticalPlane());
		}
		select(player, pattern, null);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}

}
