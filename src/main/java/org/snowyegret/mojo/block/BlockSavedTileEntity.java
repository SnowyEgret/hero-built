package org.snowyegret.mojo.block;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class BlockSavedTileEntity extends TileEntity {

	private String path;

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

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

}
