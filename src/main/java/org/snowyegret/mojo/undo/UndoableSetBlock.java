package org.snowyegret.mojo.undo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import org.snowyegret.mojo.player.Player;

public class UndoableSetBlock implements IUndoable {

	BlockPos pos;
	IBlockState prevState, state;
	private static final String KEY_POS = "a";
	private static final String KEY_PREV_STATE = "b";
	private static final String KEY_STATE = "c";

	// For Transaction.fromNBT which can't be static if part of an interface (except in 1.8)
	// http://stackoverflow.com/questions/21817/why-cant-i-declare-static-methods-in-an-interface
	UndoableSetBlock() {
	}

	public UndoableSetBlock(BlockPos pos, IBlockState prevState, IBlockState state) {
		this.pos = pos;
		this.prevState = prevState;
		this.state = state;
	}

	// IUndoable--------------------------------------------------------------------------

	public boolean dO(Player player) {
		//UndoableSetBlock setBlock = (UndoableSetBlock) u;
		//BlockPos pos = setBlock.pos;
		World world = player.getWorld().getWorld();

		Block blockToSet = state.getBlock();
		Block blockToReplace = player.getWorld().getBlock(pos);

		// Implementation of Issue #162: Only set IPlantable if block below can sustain plant
		// Set plantables on block above.
		// Do not set if the block below cannot sustain a plant
		// System.out.println("blockToSet=" + blockToSet);
		if (blockToSet instanceof IPlantable) {
			if (!blockToReplace.canSustainPlant(world, pos, EnumFacing.UP, (IPlantable) blockToSet)) {
				return false;
			}
			pos = pos.up();
			//setBlock.pos = pos;
		}

		// TODO Issue: Torches should be placed on block side (not replace the block) #164
		// Not finished!
		if (blockToSet instanceof BlockTorch) {
			System.out.println("Got a torch");
			// if (block == Blocks.air || !block.canPlaceBlockOnSide(w, pos, EnumFacing.UP)) {
			// if (block == Blocks.air) {
			// continue;
			// }
			// pos = pos.up();
			// setBlock.pos = pos;
		}

		// Check for bounds, and call Block#isReplaceable and Block#canReplace
		// Player is expected to break his way out.
		// If the player reselects, when the player deselects the broken blocks will reappear.
		// From World#canBlockBePlaced which does not give reason block cannot be placed
		// What is intent of third parameter
		AxisAlignedBB bb = blockToSet.getCollisionBoundingBox(world, pos, blockToSet.getDefaultState());
		if (bb != null && !world.checkNoEntityCollision(bb, null)) {
			System.out.println("Collision with player. blockToSet=" + blockToSet);
			return false;
		}

		// Do not understand parameters.
		// if (!blockToSet.canReplace(world, pos, EnumFacing.UP, (ItemStack) null)) {
		// // if (!blockToReplace.getMaterial().isReplaceable()) {
		// System.out.println("Material not replaceable. material=" + blockToReplace.getMaterial().getClass());
		// continue;
		// }

		// TODO
		// ItemBlock.setTileEntityNBT
		
		// TODO
		//return player.getWorld().setState(pos, state);
		player.getWorld().setState(pos, state);
		return true;
	}

	@Override
	public void undo(Player player) {
		player.getWorld().setState(pos, prevState);
	}

	@Override
	public void redo(Player player) {
		dO(player);
		//player.getWorld().setState(pos, state);
	}

	// Implemented to save a large transaction to disk.
	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong(KEY_POS, pos.toLong());
		tag.setInteger(KEY_PREV_STATE, Block.getStateId(prevState));
		tag.setInteger(KEY_STATE, Block.getStateId(state));
		return tag;
	}

	@Override
	public IUndoable fromNBT(NBTTagCompound tag) {
		pos = BlockPos.fromLong(tag.getLong(KEY_POS));
		prevState = Block.getStateById(tag.getInteger(KEY_PREV_STATE));
		state = Block.getStateById(tag.getInteger(KEY_STATE));
		return new UndoableSetBlock(pos, prevState, state);
	}

	// --------------------------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((prevState == null) ? 0 : prevState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UndoableSetBlock other = (UndoableSetBlock) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (prevState == null) {
			if (other.prevState != null)
				return false;
		} else if (!prevState.equals(other.prevState))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlock [pos=");
		builder.append(pos);
		builder.append(", state=");
		builder.append(state);
		builder.append(", prevState=");
		builder.append(prevState);
		builder.append("]");
		return builder.toString();
	}
}
