package org.snowyegret.mojo.item.spell.condition;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.world.IWorld;

public class IsOnEdgeOnGround implements ICondition {

	@Override
	public boolean apply(IWorld world, BlockPos pos, Block patternBlock) {
		if (world.getBlock(pos.up()) != Blocks.air) {
			return false;
		}
		// Test for interior edges
		for (BlockPos p : Select.ABOVE) {
			p = p.add(pos);
			if (world.getBlock(p) != Blocks.air) {
				return true;
			}
		}
		// Test for exterior edges
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
