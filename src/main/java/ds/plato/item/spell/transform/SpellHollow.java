package ds.plato.item.spell.transform;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.init.Blocks;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.geom.VoxelSet;
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
				List surroundingPoints = new ArrayList();
				surroundingPoints.add(new Point3i(s.x + 1, s.y, s.z));
				surroundingPoints.add(new Point3i(s.x - 1, s.y, s.z));
				surroundingPoints.add(new Point3i(s.x, s.y + 1, s.z));
				surroundingPoints.add(new Point3i(s.x, s.y - 1, s.z));
				surroundingPoints.add(new Point3i(s.x, s.y, s.z + 1));
				surroundingPoints.add(new Point3i(s.x, s.y, s.z - 1));
				if (selections.containsAll(surroundingPoints)) {
					s.block = Blocks.air;
				}
				return s;
			}
		});
	}

}
