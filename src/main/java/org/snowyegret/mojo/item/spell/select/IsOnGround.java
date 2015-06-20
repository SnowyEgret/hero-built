package org.snowyegret.mojo.item.spell.select;

import org.snowyegret.mojo.item.spell.ICondition;
import org.snowyegret.mojo.world.IWorld;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class IsOnGround implements ICondition {

	@Override
	public boolean test(IWorld world, BlockPos pos) {
		if(world.getBlock(pos.up()) == Blocks.air) {
			return true;
		}
		return false;
	}

}
