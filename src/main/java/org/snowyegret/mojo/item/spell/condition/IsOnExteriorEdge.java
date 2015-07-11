package org.snowyegret.mojo.item.spell.condition;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class IsOnExteriorEdge implements ICondition {

	// TODO SelectEdge cannot select a diagonal edge #236
	// Using Select.ALL and testing for size > 9 fails when thickness of a plane is two or less
	@Override
	public boolean apply(IWorld world, BlockPos pos, Block patternBlock) {
		List<BlockPos> airBlockPositions = Lists.newArrayList();
		for (BlockPos p : Select.XYZ) {
			// for (BlockPos p : Select.ALL) {
			p = p.add(pos);
			if (world.getBlock(p) == Blocks.air) {
				airBlockPositions.add(p);
			}
		}
		int size = airBlockPositions.size();
		// System.out.println("size=" + size);
		// if (size > 9) {
		// if (size > 17 && size < 20) {
		// return false;
		// } else {
		// return true;
		// }
		// }

		if (size > 2) {
			return true;
		}
		// Could be on either side of a flat plate
		if (size == 2) {
			double d = airBlockPositions.get(0).distanceSq(airBlockPositions.get(1));
			if (d < 3) {
				return true;
			}
		}

		// Number of air blocks is one or zero
		return false;
	}
}
