package ds.plato.item.spell.transform;

import net.minecraft.init.Blocks;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.select.Selection;

public class SpellDelete extends AbstractSpellTransform {


	public SpellDelete(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selectionManager's selection list.
				return new Selection(s.x, s.y, s.z, Blocks.air, 0);
			}
		});
	}
}
