package ds.plato.item.spell.transform;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarDistribution;
import ds.plato.core.HotbarSlot;
import ds.plato.select.Selection;

public class SpellFillRandom extends AbstractSpellTransform {

	public SpellFillRandom(IUndo undo, ISelect select, IPick pick) {
		super(undo, select, pick);
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot...slots) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				HotbarDistribution d = new HotbarDistribution(slots);
				HotbarSlot entry = d.randomEntry();
				s.setBlock(entry.block);
				//s.metadata = entry.metadata;
				return s;
			}
		});
	}
}
