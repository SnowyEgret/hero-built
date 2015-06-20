package org.snowyegret.mojo.item.spell;

import org.snowyegret.mojo.world.IWorld;

import net.minecraft.util.BlockPos;

public interface ICondition {
	public boolean test(IWorld world, BlockPos pos);
}
