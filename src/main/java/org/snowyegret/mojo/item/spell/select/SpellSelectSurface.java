package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

public class SpellSelectSurface extends AbstractSpellSelect {

	public SpellSelectSurface() {
		super(Select.ALL);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(Player player) {
		EnumFacing side = player.getPickManager().firstPick().side;
		boolean ignoreSide = player.getModifiers().isPressed(Modifier.SHIFT);
		setSelectionPattern(ignoreSide ? Select.ALL : Select.planeForSide(side));
		setConditions(new IsOnSurface(side, ignoreSide));	
		super.invoke(player);
	}
	
}
