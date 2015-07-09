package org.snowyegret.mojo.item.spell.other;

import java.util.Random;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.condition.ICondition;
import org.snowyegret.mojo.item.spell.condition.IsOnCorner;
import org.snowyegret.mojo.item.spell.condition.IsOnEdge;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Sets;

public class SpellErode extends Spell {

	private final Random random = new Random();

	public SpellErode() {
		super(1);
	}

	@Override
	public void invoke(Player player) {
		Iterable<Selection> selections = player.getSelections();
		player.clearSelections();
		player.clearPicks();

		Set<IUndoable> undoables = Sets.newHashSet();
		IBlockState air = Blocks.air.getDefaultState();
		ICondition isOnCorner = new IsOnCorner();
		ICondition isOnEdge = new IsOnEdge();
		IWorld w = player.getWorld();
		for (Selection s : selections) {
			BlockPos pos = s.getPos();
			if (isOnCorner.apply(w, pos, Blocks.air)) {
				undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), air));
			}
			if (isOnEdge.apply(w, pos, Blocks.air)) {
				if (random.nextInt(10) < 3) {
					undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), air));
				}
			}
		}

		player.getTransactionManager().doTransaction(undoables);
	}
}
