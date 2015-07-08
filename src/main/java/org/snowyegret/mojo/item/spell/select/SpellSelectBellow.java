package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

public class SpellSelectBellow extends AbstractSpellSelect {

	public SpellSelectBellow() {
		info.addModifiers(Modifier.SHIFT);
	}
	
	@Override
	public void invoke(Player player) {
		BlockPos[] pattern = player.getModifiers().isPressed(Modifier.SHIFT) ? Select.BELOW_INCLUSIVE : Select.DOWN;
		select(player, pattern, null);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
