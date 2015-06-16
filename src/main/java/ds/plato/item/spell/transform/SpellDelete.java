package ds.plato.item.spell.transform;

import net.minecraft.init.Blocks;

import com.google.common.collect.Lists;

import ds.plato.player.IPlayer;
import ds.plato.select.Selection;

public class SpellDelete extends AbstractSpellTransform {

	@Override
	public void invoke(IPlayer player) {
		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
				// Create a copy here because we don't want to modify the selectionManager's selection list.
				//If we don't create a copy undo doesn't work
				return Lists.newArrayList(new Selection(s.getPos(), Blocks.air.getDefaultState()));
			}
		});
	}
	
}
