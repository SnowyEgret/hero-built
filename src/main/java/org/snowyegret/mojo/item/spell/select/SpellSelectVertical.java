package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

public class SpellSelectVertical extends AbstractSpellSelect {

	public SpellSelectVertical() {
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(Player player) {
		// TODO Use player diretion to determine vertical plane in SpellSelectVertical #223
		BlockPos[] pattern = player.getModifiers().isPressed(Modifier.SHIFT) ? Select.NORTH_SOUTH : Select.EAST_WEST;
		select(player, pattern, null);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}

}
