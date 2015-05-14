package ds.plato.item.spell.select;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.plato.api.IWorld;

public class IsOnSurface implements ICondition {

	@Override
	public boolean test(IWorld world, BlockPos pos) {
		for (BlockPos p : Select.all) {
			p = p.add(pos);
			if (world.getBlock(p) == Blocks.air) {
				return true;
			}
		}
		return false;
	}

}
