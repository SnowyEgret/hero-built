package org.snowyegret.mojo.item.spell.condition;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class IsOnCorner implements ICondition {

	@Override
	public boolean apply(IWorld world, BlockPos pos, Block patternBlock) {
		List<BlockPos> airBlockPositions = Lists.newArrayList();
		for (BlockPos p : Select.XYZ) {
			p = p.add(pos);
			if (world.getBlock(p) == Blocks.air) {
				airBlockPositions.add(p);
			}
		}
		if (airBlockPositions.size() > 2) {
			return true;
		}
		return false;
	}

}
