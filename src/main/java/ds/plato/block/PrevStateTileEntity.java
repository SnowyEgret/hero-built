package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class PrevStateTileEntity extends TileEntity {

	private IBlockState prevState;

	public IBlockState getPrevState() {
		return prevState;
	}

	public void setPrevState(IBlockState state) {
		prevState = state;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		int id = tag.getInteger("id");
		prevState = Block.getStateById(id);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		if (prevState == null) {
			System.out.println("prevState=" + prevState);
		} else {
			tag.setInteger("id", Block.getStateId(prevState));
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PrevStateTileEntiy [prevState=");
		builder.append(prevState);
		builder.append("]");
		return builder.toString();
	}

}
