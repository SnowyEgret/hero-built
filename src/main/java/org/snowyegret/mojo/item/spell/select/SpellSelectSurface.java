package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.IPlayer;

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
	public void invoke(IPlayer player) {
		EnumFacing side = player.getPickManager().firstPick().side;
		boolean ignoreSide = player.getModifiers().isPressed(Modifier.SHIFT);
		setGrowthPattern(ignoreSide ? Select.ALL : Select.planeForSide(side));
//		boolean ignoreSide = false;
//		if(player.getModifiers().isPressed(Modifier.SHIFT)) {
//			setGrowthPattern(Select.ALL);
//			ignoreSide = true;
//		} else {
//			setGrowthPattern(Select.planeForSide(side));
//		}
		setConditions(new IsOnSurface(side, ignoreSide));	
		super.invoke(player);
	}
	
}
