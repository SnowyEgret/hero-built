package ds.plato.item.spell.transform;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

import ds.geom.VoxelSet;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;

public class SpellHollow extends AbstractSpellTransform {

	@Override
	public void invoke(IPlayer player) {
		final ISelect selectionManager = player.getSelectionManager();
		final IBlockState air = Blocks.air.getDefaultState();
		transformSelections(player, new ITransform() {
			VoxelSet voxels = selectionManager.voxelSet();

			@Override
			public Iterable<Selection> transform(Selection s) {
				//TODO BlockPos.getAllInBox(from, to);
				BlockPos pos = s.getPos();
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();
				List surroundingPoints = new ArrayList();
				surroundingPoints.add(new Point3i(x + 1, y, z));
				surroundingPoints.add(new Point3i(x - 1, y, z));
				surroundingPoints.add(new Point3i(x, y + 1, z));
				surroundingPoints.add(new Point3i(x, y - 1, z));
				surroundingPoints.add(new Point3i(x, y, z + 1));
				surroundingPoints.add(new Point3i(x, y, z - 1));
				if (voxels.containsAll(surroundingPoints)) {
					s = new Selection(pos, air);
				}
				return Lists.newArrayList(s);
			}
		});
	}
}
