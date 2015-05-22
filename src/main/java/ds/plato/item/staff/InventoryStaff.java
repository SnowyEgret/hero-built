package ds.plato.item.staff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class InventoryStaff implements IInventory {

	private TagStaff tag;
	//private int size = Staff.maxNumSpells;
	//These three fields to be used in method setInventorySlotContents
	private IInventory inventory;
	private ItemStack stack;
	private int slot;

	public InventoryStaff(IInventory inventoryContainingStaff, int slotInInventoryContainingStaff) {
		this.inventory = inventoryContainingStaff;
		this.slot = slotInInventoryContainingStaff;
		stack = inventoryContainingStaff.getStackInSlot(slotInInventoryContainingStaff);
		tag = new TagStaff(stack);
//		NBTTagCompound t = stack.getTagCompound();
//		if (t == null) {
//			t = new NBTTagCompound();
//			stack.setTagCompound(t);
//		}
//		tag = new TagStaff(t, size);
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
		if (tag.getItemStack(i) != null) {
			ItemStack itemstack = tag.getItemStack(i);
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
		inventory.setInventorySlotContents(slot, this.stack);
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
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			setInventorySlotContents(i, null);
		}
	}
}