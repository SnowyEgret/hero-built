package ds.plato.pick;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.api.IPick;
import ds.plato.api.IWorld;
import ds.plato.block.BlockPicked;

public class PickManager implements IPick {

	private final LinkedList<Pick> picks = new LinkedList<>();
	private int maxPicks = 0;
	private IWorld world;
	private Block blockPicked;

	public PickManager(Block blockPicked) {
		this.blockPicked = blockPicked;
	}

	@Override
	public Pick pick(IWorld world, BlockPos pos, EnumFacing side) {
		// TODO: Handle case where location is a selection
		this.world = world;
		Block block = world.getBlock(pos);
		world.setBlock(pos, blockPicked);
		return addPick(pos, block, side);
	}

	@Override
	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	@Override
	public Pick getPickAt(BlockPos pos) {
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
				world.setBlock(p.getPos(), p.block);
			}
		}
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

	//Default - also used by test class---------------------------------------------------------------
	
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
