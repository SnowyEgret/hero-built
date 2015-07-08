package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.player.Player;

public class SpellSelectHorizontal extends AbstractSpellSelect {

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

	@Override
	public void invoke(Player player) {
		select(player, Select.HORIZONTAL, null);
	}
}
