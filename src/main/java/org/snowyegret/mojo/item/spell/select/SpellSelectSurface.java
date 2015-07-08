package org.snowyegret.mojo.item.spell.select;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.ICondition;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.player.Player;

import com.google.common.collect.Lists;

public class SpellSelectSurface extends AbstractSpellSelect {

	public SpellSelectSurface() {
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
		BlockPos[] pattern = ignoreSide ? Select.ALL : Select.planeForSide(side);
		ICondition condition = new IsOnSurface(side, ignoreSide);
		select(player, pattern, Lists.newArrayList(condition));
	}
	
}
