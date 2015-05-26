package ds.plato.pick;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.block.BlockPicked;
import ds.plato.select.ISelect;
import ds.plato.world.IWorld;

public class PickManager implements IPick {

	private final LinkedList<Pick> picks = new LinkedList<>();
	private LinkedList<Pick> lastPicks;
	private int maxPicks = 0;
	private IWorld world;
	private Block blockPicked;
	private ISelect selectionManager;

	public PickManager(Block blockPicked, ISelect selectionManager) {
		this.blockPicked = blockPicked;
		this.selectionManager = selectionManager;
	}

	@Override
	public Pick pick(IWorld world, BlockPos pos, EnumFacing side) {
		this.world = world;
		Block block = world.getBlock(pos);
		// TODO Handle case where picked block is already selected
		// SpellCopy works ok without this
		// if (block instanceof BlockSelected) {
		// Selection s = selectionManager.getSelection(pos);
		// if (s != null) {
		// block = s.getBlock();
		// }
		// }
		world.setBlock(pos, blockPicked);
		return addPick(pos, block, side);
	}

	@Override
	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	@Override
	public Pick getPick(BlockPos pos) {
		for (Pick p : picks) {
			if (p.equals(new Pick(pos, null, null))) {
				return p;
			}
		}
		return null;
	}

	@Override
	public void clearPicks() {
		for (Pick p : getPicks()) {
			Block block = world.getBlock(p.getPos());
			if (block instanceof BlockPicked) {
				world.setBlock(p.getPos(), p.getBlock());
			}
		}
		lastPicks = new LinkedList();
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
	public void repick() {
		//clearPicks();
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

	Pick addPick(BlockPos pos, Block block, EnumFacing side) {
		if (picks.size() < maxPicks) {
			Pick p = new Pick(pos, block, side);
			picks.add(p);
			return p;
		} else {
			return null;
		}
	}
}
