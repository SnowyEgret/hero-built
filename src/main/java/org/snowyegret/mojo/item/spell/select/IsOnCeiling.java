package org.snowyegret.mojo.item.spell.select;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.ICondition;
import org.snowyegret.mojo.world.IWorld;

public class IsOnCeiling implements ICondition {

	@Override
	public boolean test(IWorld world, BlockPos pos, Block patternBlock) {
		if(world.getBlock(pos.down()) == Blocks.air) {
			return true;
		}
		return false;
	}

}
