package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.player.Player;


public class SpellSelectAll extends AbstractSpellSelect {

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}

	@Override
	public void invoke(Player player) {
		select(player, Select.ALL, null );
	}
}
