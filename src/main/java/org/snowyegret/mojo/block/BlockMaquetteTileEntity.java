package org.snowyegret.mojo.block;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.select.Selection;

import com.google.common.collect.Lists;

public class BlockMaquetteTileEntity extends TileEntity {

	// See comment in #getDrops
	public static final String KEY_TAG = "BlockEntityTag";
	public static final String KEY_SIZE = "size";
	public static final String KEY_NAME = "name";
	public static final String KEY_ORIGIN = "origin";
	public static final String EXTENTION = ".maquette";

	private String name;
	private BlockPos origin;
	private List<Selection> selections;

	// private NBTTagCompound tag;
	//
	// public void setTag(NBTTagCompound tag) {
	// this.tag = tag;
	// }
	//
	// public NBTTagCompound getTag() {
	// return tag;
	// }

	// TODO we had to do this for PrevStateTileEntity
	// When selecting a BlockSaved its tile entity is deleted.
	// For now, just prohibit selection of BlockMaquette in SelectionManager
	// @Override
	// public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	// return oldState.getBlock() != newState.getBlock() || newState.getBlock() instanceof BlockSelected;
	// }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setOrigin(BlockPos origin) {
		this.origin = origin;
	}

	public void setSelections(List<Selection> selections) {
		this.selections = selections;
	}

	public Iterable<Selection> getSelections() {
		return selections;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		System.out.println("before tag=" + tag);
		// if (tagIn == null) {
		// tagIn = new NBTTagCompound();
		// }
		// if (tag != null) {
		// // FIXME tag is null!
		// tagIn.setTag(BlockMaquette.KEY_TAG, tag);
		// } else {
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>Could not set tag on tagIn. tag=" + tag);
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>tile data" + getTileData());
		// }

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
		System.out.println("after tag=" + tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		System.out.println("tag=" + tag);
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
		System.out.println("tag=" + tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		super.writeToNBT(tag);
		System.out.println("tag=" + tag);
		return new S35PacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		System.out.println("tag=" + tag);
		readFromNBT(tag);
		super.readFromNBT(tag);
		System.out.println(this);
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
