package ds.plato.item.spell.select;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.plato.item.spell.ICondition;
import ds.plato.world.IWorld;

public class IsOnCeiling implements ICondition {

	@Override
	public boolean test(IWorld world, BlockPos pos) {
		if(world.getBlock(pos.down()) == Blocks.air) {
			return true;
		}
		return false;
	}

}
