package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.player.Player;

public class SpellSelectDown extends AbstractSpellSelect {

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}

	@Override
	public void invoke(Player player) {
		select(player, Select.DOWN, null);
	}
}
