package org.snowyegret.plato.item.spell.select;

import org.snowyegret.plato.item.spell.ICondition;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class IsOnEdgeOnGround implements ICondition {

	@Override
	public boolean test(IWorld world, BlockPos pos) {
		if(world.getBlock(pos.up()) != Blocks.air) {
			return false;
		}
		for (BlockPos p : Select.ABOVE) {
			p = p.add(pos);
			if (world.getBlock(p) != Blocks.air) {
				return true;
			}
		}
		return false;
	}

}
