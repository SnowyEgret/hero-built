package ds.plato.item.spell.transform;

import ds.plato.pick.IPick;
import ds.plato.player.HotbarDistribution;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

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
