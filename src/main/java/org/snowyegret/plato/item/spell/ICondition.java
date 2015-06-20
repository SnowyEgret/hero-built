package org.snowyegret.plato.item.spell;

import org.snowyegret.plato.world.IWorld;

import net.minecraft.util.BlockPos;

public interface ICondition {
	public boolean test(IWorld world, BlockPos pos);
}
