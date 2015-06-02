package ds.plato.item.spell;

import net.minecraft.util.BlockPos;
import ds.plato.world.IWorld;

public interface ICondition {
	public boolean test(IWorld world, BlockPos pos);
}
