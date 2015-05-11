package ds.plato.item.spell.transform;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.player.HotbarSlot;
import ds.plato.select.Selection;

public class SpellFill extends AbstractSpellTransform {

	public SpellFill(IUndo undo, ISelect select, IPick pick) {
		super(undo, select, pick);
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot...slotEntries) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selection list.
				// Use first (left-most) block in inventory
				HotbarSlot e = slotEntries[0];
				return new Selection(s.getPos(), e.block);
			}
		});
	}
}
