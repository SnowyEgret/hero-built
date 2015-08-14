package org.snowyegret.mojo.block;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import org.snowyegret.mojo.select.Selection;

import com.google.common.collect.Lists;

public class BlockMaquetteTileEntity extends TileEntity {

	public static final String KEY_SIZE = "size";
	public static final String KEY_NAME = "name";
	public static final String KEY_ORIGIN = "origin";

	private String name;
	private BlockPos origin;
	private List<Selection> selections;

	// We had to do this for PrevStateTileEntity
	// @Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setOrigin(BlockPos origin) {
		this.origin = origin;
	}

	public BlockPos getOrigin() {
		return origin;
	}

	public void setSelections(List<Selection> selections) {
		this.selections = selections;
	}

	public Iterable<Selection> getSelections() {
		return selections;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		//System.out.println("this=" + this);
		if (name != null && !name.isEmpty()) {
			tag.setString(KEY_NAME, name);
		}
		if (origin != null) {
			tag.setLong(KEY_ORIGIN, origin.toLong());
		}
		if (selections != null) {
			tag.setInteger(KEY_SIZE, selections.size());
			int i = 0;
			for (Selection s : selections) {
				tag.setTag(String.valueOf(i), s.toNBT());
				i++;
			}
		}
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		name = tag.getString(KEY_NAME);
		origin = BlockPos.fromLong(tag.getLong(KEY_ORIGIN));
		selections = Lists.newArrayList();
		int size = tag.getInteger(KEY_SIZE);
		// tag = (NBTTagCompound) tag.getTag(KEY_TAG);
		for (int i = 0; i < size; i++) {
			Selection s = Selection.fromNBT(tag.getCompoundTag(String.valueOf(i)));
			selections.add(s);
		}
		super.readFromNBT(tag);
		//System.out.println("this=" + this);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		super.writeToNBT(tag);
		// System.out.println("tag=" + tag);
		return new S35PacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		// System.out.println("tag=" + tag);
		readFromNBT(tag);
		super.readFromNBT(tag);
		// System.out.println(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockMaquetteTileEntity [name=");
		builder.append(name);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", selections=");
		builder.append(selections);
		builder.append("]");
		return builder.toString();
	}

}
