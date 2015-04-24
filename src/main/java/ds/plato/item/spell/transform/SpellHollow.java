package ds.plato.item.spell.transform;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import ds.geom.VoxelSet;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.select.Selection;

public class SpellHollow extends AbstractSpellTransform {

	public SpellHollow(IUndo undo, ISelect select, IPick pick) {
		super(undo, select, pick);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		transformSelections(world, new ITransform() {
			VoxelSet selections = selectionManager.voxelSet();

			@Override
			public Selection transform(Selection s) {
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
				if (selections.containsAll(surroundingPoints)) {
					s.setBlock(Blocks.air);
				}
				return s;
			}
		});
	}

}
