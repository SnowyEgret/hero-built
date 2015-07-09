package org.snowyegret.mojo.item.spell.condition;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.world.IWorld;

public interface ICondition {
	public boolean apply(IWorld world, BlockPos pos, Block patternBlock);
}
