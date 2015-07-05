package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.player.Player;


public class SpellSelectAbove extends AbstractSpellSelect {

	public SpellSelectAbove() {
		super(Select.ABOVE_INCLUSIVE);
	}

	@Override
	public void invoke(Player player) {
		if (player.getModifiers().isPressed(Modifier.SHIFT)) {
			setSelectionPattern(Select.ABOVE);
		} else {
			setSelectionPattern(Select.ABOVE_INCLUSIVE);
		}
		super.invoke(player);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
