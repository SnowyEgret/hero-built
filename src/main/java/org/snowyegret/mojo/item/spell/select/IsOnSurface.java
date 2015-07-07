package org.snowyegret.mojo.item.spell.select;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.ICondition;
import org.snowyegret.mojo.world.IWorld;

public class IsOnSurface implements ICondition {

	private EnumFacing side;
	private boolean ignoreSide;

	public IsOnSurface(EnumFacing side, boolean ignoreSide) {
		this.side = side;
		this.ignoreSide = ignoreSide;
	}

	@Override
	public boolean test(IWorld world, BlockPos pos, Block patternBlock) {
		if (ignoreSide) {
			for (BlockPos p : Select.UP_DOWN_NESW) {
				p = p.add(pos);
				if (world.getBlock(p) == Blocks.air) {
					return true;
				}
			}
		} else {
			Block b = world.getBlock(pos.offset(side));
			if (b == Blocks.air) {
				return true;
			}
			//For selecting under plants, etc
			if (!b.isNormalCube()) {
				return true;
			}
		}

		// For detecting if is on edge
		// for (BlockPos p : Select.toSideOfPlane(side)) {
		// p = p.add(pos);
		// if (world.getBlock(p) == Blocks.air) {
		// return true;
		// }
		// }
		return false;
	}

}
