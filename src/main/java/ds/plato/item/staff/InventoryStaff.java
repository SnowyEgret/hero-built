package ds.plato.item.staff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class InventoryStaff implements IInventory {

	private TagStaff tag;
	
	//These three fields to be used in method setInventorySlotContents
	private IInventory parentInventory;
	private ItemStack parentStack;
	private int parentSlot;

	public InventoryStaff(IInventory parentInventory, int parentSlot) {
		this.parentInventory = parentInventory;
		this.parentSlot = parentSlot;
		parentStack = parentInventory.getStackInSlot(parentSlot);
		tag = new TagStaff(parentStack);
	}
	
	//IInventory--------------------------------------------------------

	@Override
	public ItemStack getStackInSlot(int i) {
		return tag.getItemStack(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int amount) {
		ItemStack stack = tag.getItemStack(i);
		tag.setItemStack(i, null);
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		//Seems like this is not being called
		System.out.println();
		if (tag.getItemStack(i) != null) {
			ItemStack itemstack = tag.getItemStack(i);
			//Why this?
			tag.setItemStack(i, null);
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		tag.setItemStack(i, stack);
		//These three fields were passed to constructor
		parentInventory.setInventorySlotContents(parentSlot, this.parentStack);
	}

	@Override
	public int getSizeInventory() {
		return Staff.maxNumSpells;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		return true;
	}

	// ---------------------------------- New 1.8 methods in IInventory

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}

	@Override
	public void openInventory(EntityPlayer player) {
		System.out.println();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		//Seems this is not being called
		System.out.println();
		for (int i = 0; i < parentInventory.getSizeInventory(); ++i) {
			setInventorySlotContents(i, null);
		}
	}
}