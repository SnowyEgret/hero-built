package org.snowyegret.mojo.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSavedTileEntity extends TileEntity {

	private String path;

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	// TODO we had to do this for PrevStateTileEntiey
	// When selecting a BlockSaved its tile entity is deleted.
	// For now, just prohibit selection of BlockSaved in SelectionManager
	// @Override
	// public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	// return oldState.getBlock() != newState.getBlock() || newState.getBlock() instanceof BlockSelected;
	// }

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		if (path != null) {
			tag.setString(BlockSaved.KEY_PATH, path);
		}
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		path = tag.getString(BlockSaved.KEY_PATH);
		super.readFromNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		super.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockSavedTileEntity [path=");
		builder.append(path);
		builder.append("]");
		return builder.toString();
	}

}
