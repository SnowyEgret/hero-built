package org.snowyegret.mojo.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class BlockMaquetteTileEntity extends TileEntity {

	private NBTTagCompound tag;

	public void setTag(NBTTagCompound tag) {
		this.tag = tag;
	}

	public NBTTagCompound getTag() {
		return tag;
	}

	// TODO we had to do this for PrevStateTileEntity
	// When selecting a BlockSaved its tile entity is deleted.
	// For now, just prohibit selection of BlockMaquette in SelectionManager
	// @Override
	// public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	// return oldState.getBlock() != newState.getBlock() || newState.getBlock() instanceof BlockSelected;
	// }

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		if (this.tag != null) {
			tag.setTag(BlockMaquette.KEY_TAG, this.tag);
		}
		super.writeToNBT(tag);
	}

	@Override  
	public void readFromNBT(NBTTagCompound tag) {
		this.tag = (NBTTagCompound) tag.getTag(BlockMaquette.KEY_TAG);
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
		builder.append("BlockMaquetteTileEntity [path=");
		builder.append(tag);
		builder.append("]");
		return builder.toString();
	}


}
