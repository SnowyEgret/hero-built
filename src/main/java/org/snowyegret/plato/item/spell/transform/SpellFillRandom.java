package org.snowyegret.plato.item.spell.transform;

import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.Selection;

import net.minecraft.block.state.IBlockState;

import com.google.common.collect.Lists;

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
