package org.snowyegret.plato.item.spell.transform;

import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.Selection;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.block.state.IBlockState;

import com.google.common.collect.Lists;

public class SpellFill extends AbstractSpellTransform {

	@Override
	public void invoke(final IPlayer player) {
		final IBlockState firstBlock = player.getHotbar().firstBlock();
		System.out.println("firstBlock=" + firstBlock);
		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
				return Lists.newArrayList(new Selection(s.getPos(), firstBlock));
			}
		});
	}

	public void invoke(IWorld world, final IPlayer player, final IBlockState state) {
		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
				return Lists.newArrayList(new Selection(s.getPos(), state));
			}
		});
	}
}
