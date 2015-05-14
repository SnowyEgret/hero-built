package ds.plato.item.spell.select;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.api.IWorld;

public class IsOnSurface implements ICondition {

	private EnumFacing side;
	private boolean ignoreSide;

	public IsOnSurface(EnumFacing side, boolean ignoreSide) {
		this.side = side;
		this.ignoreSide = ignoreSide;
	}

	@Override
	public boolean test(IWorld world, BlockPos pos) {
		if (ignoreSide) {
			for (BlockPos p : Select.upDownNorhEastSouthWest) {
				p = p.add(pos);
				if (world.getBlock(p) == Blocks.air) {
					return true;
				}
			}
		} else {
			if (world.getBlock(pos.offset(side)) == Blocks.air) {
				return true;
			}
		}

		// For detecting if is on edge
		// for (BlockPos p : Select.toSideOfPlane(side)) {
		// p = p.add(pos);
		// if (world.getBlock(p) == Blocks.air) {
		// return true;
		// }
		// }
		return false;
	}

}
