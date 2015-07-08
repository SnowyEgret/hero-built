package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.player.Player;

public class SpellSelectUp extends AbstractSpellSelect {

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", "   ", 'A', ingredientA, 'B', ingredientB };
	}

	@Override
	public void invoke(Player player) {
		select(player, Select.UP, null);
	}

}
