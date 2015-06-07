package ds.plato.pick;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockSelected;
import ds.plato.select.ISelect;
import ds.plato.world.IWorld;

public class PickManager implements IPick {

	private LinkedList<Pick> picks = new LinkedList<>();
	private LinkedList<Pick> lastPicks = new LinkedList<>();
	private int maxPicks = 0;
	private Block blockPicked;
	private ISelect selectionManager;

	public PickManager(Block blockPicked, ISelect selectionManager) {
		this.blockPicked = blockPicked;
		this.selectionManager = selectionManager;
	}

	@Override
	public Pick pick(IWorld world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getActualState(pos);

		// This is preventing isAmbientOcclustion crash (but still missing a pick) when repicking after spellCopy in MP
		// but is making second pick
		// out of bounds using arrows to copy in SP
//		if (state.getBlock() instanceof BlockPicked) {
//			// Even though picks may have been cleared the state may not be set yet.
//			// getPick is already null so we have no way of knowing what the original block was
//			Pick p = getPick(pos);
//			System.out.println("pick=" + p);
//			return p;
//		}

		// SpellCopy is repicking so the copy can be repeated. Method addPick is returning null because
		// maxPicks is already reached. We don't want a BlockPicked in the world when there is no
		// corresponding pick at that position in the PickManager.
		Pick pick = addPick(pos, state, side);
		if (pick != null) {
			world.setState(pos, blockPicked.getDefaultState());
		}
		return pick;
	}

	@Override
	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	@Override
	public Pick getPick(BlockPos pos) {
		for (Pick p : picks) {
			if (pos.equals(p.getPos())) {
				return p;
			}
		}
		return null;
	}

	@Override
	public void clearPicks(IWorld world) {
		for (Pick p : getPicks()) {
			IBlockState state = world.getActualState(p.getPos());
			//Why are doing this test?
			//Commented out as fix for: Second pick not cleared after a spell #102
			//if (state.getBlock() instanceof BlockPicked) {
				world.setState(p.getPos(), p.getState());
			//}
		}
		lastPicks.clear();
		lastPicks.addAll(picks);
		picks.clear();
	}

	@Override
	public boolean isPicking() {
		return picks.size() > 0 && !isFinishedPicking();
	}

	@Override
	public boolean isFinishedPicking() {
		return (picks.size() == maxPicks);
	}

	@Override
	public void reset(int maxPicks) {
		this.maxPicks = maxPicks;
		picks.clear();
	}

	@Override
	public void repick(IWorld world) {
		if (lastPicks != null) {
			for (Pick p : lastPicks) {
				pick(world, p.getPos(), p.side);
			}
		}
	}

	@Override
	public Pick lastPick() {
		try {
			return picks.getLast();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PickManager [maxPicks=");
		builder.append(maxPicks);
		builder.append(", picks=");
		builder.append(picks);
		builder.append("]");
		return builder.toString();
	}

	// Default - also used by test class---------------------------------------------------------------

	private Pick getPick(int i) {
		return picks.get(i);
	}

	private int size() {
		return picks.size();
	}

	@Deprecated
	Pick addPick(BlockPos pos, Block block, EnumFacing side) {
		if (picks.size() < maxPicks) {
			Pick p = new Pick(pos, block, side);
			picks.add(p);
			return p;
		} else {
			return null;
		}
	}

	Pick addPick(BlockPos pos, IBlockState state, EnumFacing side) {
		if (picks.size() < maxPicks) {
			Pick p = new Pick(pos, state, side);
			picks.add(p);
			return p;
		} else {
			System.out.println("Already reached maxPicks. Returning nulll");
			return null;
		}
	}
}
