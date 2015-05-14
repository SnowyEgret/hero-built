package ds.plato.item.spell.select;

import net.minecraft.util.BlockPos;
import ds.plato.api.IWorld;

public interface ICondition {
	public boolean test(IWorld world, BlockPos pos);
}
