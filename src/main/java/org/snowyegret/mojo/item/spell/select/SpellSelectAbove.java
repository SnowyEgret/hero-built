package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

public class SpellSelectAbove extends AbstractSpellSelect {

	public SpellSelectAbove() {
		info.addModifiers(Modifier.SHIFT);
	}
	
	@Override
	public void invoke(Player player) {
		BlockPos[] pattern = player.getModifiers().isPressed(Modifier.SHIFT) ? Select.ABOVE_INCLUSIVE : Select.UP;
		select(player, pattern, null);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
