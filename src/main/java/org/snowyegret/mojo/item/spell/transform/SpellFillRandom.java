package org.snowyegret.mojo.item.spell.transform;

import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;

import net.minecraft.block.state.IBlockState;

import com.google.common.collect.Lists;

public class SpellFillRandom extends AbstractSpellTransform {

	@Override
	public void invoke(final Player player) {
		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
				IBlockState state = player.getHotbar().randomBlock();
				return Lists.newArrayList(new Selection(s.getPos(), state));
			}
		});
	}
}
