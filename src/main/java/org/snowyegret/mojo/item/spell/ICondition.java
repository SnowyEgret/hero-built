package org.snowyegret.mojo.item.spell;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.world.IWorld;

public interface ICondition {
	public boolean test(IWorld world, BlockPos pos, Block patternBlock);
}
