package org.snowyegret.mojo.select;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class Selection {

	private BlockPos pos;
	private IBlockState state;
	private static final String KEY_POS = "pos";
	private static final String KEY_STATE = "state";

	public Selection(BlockPos pos, IBlockState state) {
		this.pos = pos;
		this.state = state;
	}

	public Point3i point3i() {
		return new Point3i(pos.getX(), pos.getY(), pos.getZ());
	}

	public Point3d point3d() {
		return new Point3d(pos.getX(), pos.getY(), pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setState(IBlockState state) {
		this.state = state;
	}

	public IBlockState getState() {
		return state;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Selection [pos=");
		builder.append(pos);
		builder.append(", state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	//TODO check that state.equals is functional
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Selection other = (Selection) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	// TODO Interface for reading and writing NBT for transaction and selections #259
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong(KEY_POS, pos.toLong());
		tag.setInteger(KEY_STATE, Block.getStateId(state));
		return tag;
	}

	public static Selection fromNBT(NBTTagCompound tag) {
		BlockPos pos = BlockPos.fromLong(tag.getLong(KEY_POS));
		IBlockState state = Block.getStateById(tag.getInteger(KEY_STATE));
		return new Selection(pos, state);
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

}
