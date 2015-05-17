package ds.plato.item.spell.transform;

import net.minecraft.util.BlockPos;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.player.HotbarSlot;
import ds.plato.select.Selection;

public class SpellFillChecker extends AbstractSpellTransform {

	public SpellFillChecker(IUndo undo,ISelect select, IPick pick) {
		super(undo, select, pick);
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot...slots) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				int i = 0;
				BlockPos pos = s.getPos();
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();
				if (((x & 1) == 0 && (z & 1) == 0) || ((x & 1) == 1 && (z & 1) == 1)) {
					i = ((y & 1) == 0) ? 0 : 1;
				} else {
					i = ((y & 1) == 0) ? 1 : 0;
				}
				s.setBlock(slots[i].block);
				return s;
			}
		});
	}
}
