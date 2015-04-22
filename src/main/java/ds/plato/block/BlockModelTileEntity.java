package ds.plato.block;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class BlockModelTileEntity extends TileEntity {

	private String model;
	public IInventory inventory = new InventoryBasic("foo", false, 9);

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.readFromNBT(tag);
		model = tag.getString("model");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.writeToNBT(tag);
		tag.setString("model", model);
	}

	// Do I have to do this? http://www.minecraftforge.net/wiki/Custom_Tile_Entity_Renderer
	// public Packet getDescriptionPacket() {
	// NBTTagCompound nbtTag = new NBTTagCompound();
	// this.writeToNBT(nbtTag);
	// return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	// }
	//
	// public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
	// readFromNBT(packet.customParam1);
	// }
	// }
}
