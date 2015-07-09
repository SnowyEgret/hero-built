package org.snowyegret.mojo.item.spell.condition;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.world.IWorld;

public class IsOnEdgeOnCeiling implements ICondition {

	@Override
	public boolean apply(IWorld world, BlockPos pos, Block patternBlock) {
		if(world.getBlock(pos.down()) != Blocks.air) {
			return false;
		}
		for (BlockPos p : Select.BELOW) {
			p = p.add(pos);
			if (world.getBlock(p) != Blocks.air) {
				return true;
			}
		}
		// Test for edge of plane
		for (BlockPos p : Select.HORIZONTAL_NO_CORNERS) {
			p = p.add(pos);
			if (world.getBlock(p) == Blocks.air) {
				// if (world.getBlock(p) != patternBlock) {
				return true;
			}
		}
		return false;
	}

}
