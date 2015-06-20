package org.snowyegret.mojo.item.spell.transform;

import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.Selection;

import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

public class SpellFillChecker extends AbstractSpellTransform {

	@Override
	public void invoke(final IPlayer player) {
		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
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
				return Lists.newArrayList(new Selection(pos, player.getHotbar().getBlock(i)));
			}
		});
	}	
}
