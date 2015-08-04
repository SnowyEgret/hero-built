package org.snowyegret.mojo.block;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class PrevStateTileEntity extends TileEntity {

	private IBlockState prevState;
	private final String PREV_STATE_KEY = "st";

	public IBlockState getPrevState() {
		return prevState;
	}

	public void setPrevState(IBlockState state) {
		prevState = state;
	}

	// Fix for Selections not rendered properly after third pick of SpellSelectAll/Below/Above #167	@Override
	// http://www.minecraftforge.net/forum/index.php/topic,32141.0.html
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		// System.out.println("oldState="+oldState);
		// System.out.println("newSate="+newSate);
		// new Throwable().printStackTrace();
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		if (prevState == null) {
			System.out.println("Could not write previous state to tag. prevState=" + prevState);
		} else {
			tag.setInteger(PREV_STATE_KEY, Block.getStateId(prevState));
			// TODO Do we have to write properties?
			// for (Object name : prevState.getPropertyNames()) {
			//
			// }

		}
		// Implementation of: Find a way to restore selected blocks to their previous state when they are left in
		// world after a crash #173
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		prevState = Block.getStateById(tag.getInteger(PREV_STATE_KEY));
		super.readFromNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		//super.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		//super.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PrevStateTileEntity [prevState=");
		builder.append(prevState);
		builder.append(", isInvalid()=");
		builder.append(isInvalid());
		builder.append("]");
		return builder.toString();
	}


}
