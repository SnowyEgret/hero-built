package org.snowyegret.plato.item.spell.select;

import org.snowyegret.plato.item.spell.ICondition;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class IsOnCeiling implements ICondition {

	@Override
	public boolean test(IWorld world, BlockPos pos) {
		if(world.getBlock(pos.down()) == Blocks.air) {
			return true;
		}
		return false;
	}

}
