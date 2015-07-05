package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

public class SpellSelectBellow extends AbstractSpellSelect {

	public SpellSelectBellow() {
		super(Select.BELOW_INCLUSIVE);
	}

	@Override
	public void invoke(Player player) {
		if (player.getModifiers().isPressed(Modifier.SHIFT)) {
			setSelectionPattern(Select.BELOW);
		} else {
			setSelectionPattern(Select.BELOW_INCLUSIVE);
		}
		super.invoke(player);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
