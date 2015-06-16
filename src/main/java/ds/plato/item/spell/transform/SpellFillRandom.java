package ds.plato.item.spell.transform;

import net.minecraft.block.state.IBlockState;

import com.google.common.collect.Lists;

import ds.plato.player.IPlayer;
import ds.plato.select.Selection;

public class SpellFillRandom extends AbstractSpellTransform {

	@Override
	public void invoke(final IPlayer player) {
		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
				IBlockState state = player.getHotbar().randomBlock();
				return Lists.newArrayList(new Selection(s.getPos(), state));
			}
		});
	}
}
